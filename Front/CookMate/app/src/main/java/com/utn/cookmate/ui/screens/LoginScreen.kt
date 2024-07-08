package com.utn.cookmate.ui.screens

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.utn.cookmate.R
import com.utn.cookmate.NotificationService
import com.utn.cookmate.connection.Server
import com.utn.cookmate.data.Ingrediente
import com.utn.cookmate.data.Paso
import com.utn.cookmate.data.Receta
import com.utn.cookmate.data.UserDataUiEvents
import com.utn.cookmate.ui.CustomAlertDialog
import com.utn.cookmate.ui.PreferencesHelper
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TextFieldComponent
import com.utn.cookmate.ui.UserInputViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.Base64

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoginScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
    val context = LocalContext.current

    userInputViewModel.appStatus.value?.registerResult?.value = "" //para que si hice un registro, pueda hacer otro mas, sino el boton de ir a registrarse me trae de vuelta a esta pantalla
    // Revisar si hay datos de inicio de sesión guardados
    LaunchedEffect(Unit) {
        if(userInputViewModel.appStatus.value?.imagenesDescargadas?.isEmpty() == true){
            Server(userInputViewModel).downloadResources()
        }
        val (savedEmail, savedPassword) = PreferencesHelper.getLoginDetails(context)
        if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            // Intentar iniciar sesión con los datos guardados
            Server(userInputViewModel).login(savedEmail, savedPassword)
        }
    }



    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(150.dp),
                        painter = painterResource(id = R.drawable.logo_nombre),
                        contentDescription = "Logo de CookMate"
                    )
                }
                //TextComponent(textValue = "Email", textSize = 12.sp)
                TextFieldComponent(
                    label = "Email",
                    onTextChanged = { userInputViewModel.onEvent(UserDataUiEvents.EmailEntered(it)) })
                Spacer(modifier = Modifier.size(20.dp))
                //TextComponent(textValue = "Clave", textSize = 12.sp)
                TextFieldComponent(
                    label = "Clave",
                    onTextChanged = { userInputViewModel.onEvent(UserDataUiEvents.PasswordEntered(it)) },
                    isPassword = true // Agregar esta línea para habilitar la funcionalidad de contraseña
                )
                Spacer(modifier = Modifier.size(20.dp))

                val shouldShowLoginFailedDialog = remember { mutableStateOf(false) }
                if (shouldShowLoginFailedDialog.value) {
                    CustomAlertDialog({userInputViewModel.appStatus?.value?.loginResponse?.value = ""},"Aviso","Credenciales invalidas","OK",userInputViewModel,shouldShowDialog = shouldShowLoginFailedDialog)
                }

                if(userInputViewModel.appStatus.value?.loginResponse?.value == "{}") {
                    shouldShowLoginFailedDialog.value = true
                } else if(userInputViewModel.appStatus.value?.loginResponse?.value != "") {
                    // Guardar las credenciales de inicio de sesión
                    userInputViewModel.appStatus.value?.emailEntered?.let {
                        userInputViewModel.appStatus.value?.passwordEntered?.let { it1 ->
                            PreferencesHelper.saveLoginDetails(
                                context,
                                it,
                                it1
                            )
                        }
                   }

                    userInputViewModel.appStatus.value?.recetasGuardadas?.clear()
                    var recetasGuardadas : JSONArray = JSONArray(userInputViewModel.appStatus.value?.loginResponse?.value)
                    for (i in 0 until recetasGuardadas.length()) {
                        var listaDePasos : MutableList<Paso> = mutableListOf<Paso>()
                        val item : JSONObject= recetasGuardadas.getJSONObject(i)
                        val nombre = item.getString("nombre")
                        val listaPasos = item.getJSONArray("pasos")
                        for (j in 0 until listaPasos.length()) {
                            val paso : JSONObject= listaPasos.getJSONObject(j)
                            val numeroPaso = paso.getInt("numero")
                            val descripcionPaso = paso.getString("descripcion")
                            val imagen = paso.getString("imagen")
                            val listaIngredientes = paso.getJSONArray("ingredientes")
                            var listaDeIngredientes : MutableList<Ingrediente> = mutableListOf<Ingrediente>()
                            val duracionPaso = if (paso.has("duracion")) paso.getInt("duracion") else null
                            for (k in 0 until listaIngredientes.length()) {
                                val ingrediente : JSONObject= listaIngredientes.getJSONObject(k)
                                val nombreIngrediente = ingrediente.getString("nombre")
                                val cantidad = ingrediente.getInt("cantidad")
                                val imagenIngrediente = ingrediente.getString("imagen")
                                var ingredienteObjeto : Ingrediente = Ingrediente(nombreIngrediente,cantidad,imagenIngrediente)
                                listaDeIngredientes.add(ingredienteObjeto)
                            }
                            var pasoObjeto : Paso = Paso(numeroPaso,descripcionPaso,imagen,listaDeIngredientes,duracionPaso)
                            listaDePasos.add(pasoObjeto)
                        }
                        var receta : Receta = Receta(nombre, listaDePasos, true)
                        userInputViewModel.appStatus?.value?.recetasGuardadas?.add(receta)
                    }

                    navController.navigate(Routes.MIS_RECETAS_SCREEN)
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                    onClick = {
                        if (userInputViewModel.isValidLoginState() && userInputViewModel.appStatus?.value?.imagenesDescargadas?.isNotEmpty() == true) {
                            userInputViewModel.appStatus?.value?.passwordEntered?.let {
                                userInputViewModel.appStatus?.value?.emailEntered?.let { it1 ->
                                    Server(userInputViewModel).login(
                                        it1,
                                        it
                                    )
                                }
                            }
                        }
                    }
                ) {
                    TextComponent(
                        textValue = "Ingresar",
                        textSize = 18.sp,
                        colorValue = Color.White
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                    onClick = {
                        navController.navigate(Routes.CREATE_USER_SCREEN)
                    }
                ) {
                    TextComponent(
                        textValue = "Registrarse",
                        textSize = 18.sp,
                        colorValue = Color.White
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(20.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                onClick = {
                    navController.navigate(Routes.ABOUT_SCREEN)
                }
            ) {
                TextComponent(
                    textValue = "Acerca de...",
                    textSize = 18.sp,
                    colorValue = Color.White
                )
            }
        }
    }
}
