package com.utn.cookmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.R
import com.utn.cookmate.connection.Server
import com.utn.cookmate.data.Ingrediente
import com.utn.cookmate.data.Paso
import com.utn.cookmate.data.Receta
import com.utn.cookmate.data.UserDataUiEvents
import com.utn.cookmate.ui.PreferencesHelper
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TextFieldComponent
import com.utn.cookmate.ui.UserInputViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.Base64

@Composable
fun LoginScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
    val context = LocalContext.current

    // Revisar si hay datos de inicio de sesión guardados
    LaunchedEffect(Unit) {
        val (savedEmail, savedPassword) = PreferencesHelper.getLoginDetails(context)
        if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            // Intentar iniciar sesión con los datos guardados
            Server(userInputViewModel).login(savedEmail, savedPassword)
        }
    }

    if(userInputViewModel.appStatus?.value?.downloadResourcesResponse?.value?.isEmpty() == true){
        Server(userInputViewModel).downloadResources()
    } else if(userInputViewModel.appStatus?.value?.imagenesDescargadas?.isEmpty() == true){
        Thread(Runnable{
            var imagenes : JSONArray = JSONArray(userInputViewModel.appStatus?.value?.downloadResourcesResponse?.value)
            for (i in 0 until imagenes.length()) {
                val item : JSONObject= imagenes.getJSONObject(i)
                val nombre = item.getString("nombre")
                val base64 = item.getString("base64")
                userInputViewModel.appStatus?.value?.imagenesDescargadas!!.put(nombre,Base64.getDecoder().decode(base64))
            }
        }).start()
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
                userInputViewModel.appStatus?.value?.registerResponse?.value = ""
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(150.dp),
                        painter = painterResource(id = R.drawable.logo_nombre),
                        contentDescription = "Logo de CookMate"
                    )
                }
                TextComponent(textValue = "Email", textSize = 12.sp)
                TextFieldComponent(
                    "Email",
                    onTextChanged = { userInputViewModel.onEvent(UserDataUiEvents.EmailEntered(it)) })
                Spacer(modifier = Modifier.size(20.dp))
                TextComponent(textValue = "Clave", textSize = 12.sp)
                TextFieldComponent(
                    "Clave",
                    onTextChanged = { userInputViewModel.onEvent(UserDataUiEvents.PasswordEntered(it)) })
                Spacer(modifier = Modifier.size(20.dp))

                if(userInputViewModel.appStatus?.value?.loginResponse?.value == "{}") {
                    TextComponent(textValue = "Login fallido!", textSize = 12.sp)
                } else if(userInputViewModel.appStatus?.value?.loginResponse?.value != "") {
                    // Guardar las credenciales de inicio de sesión
                    userInputViewModel.appStatus?.value?.emailEntered?.let {
                        userInputViewModel.appStatus?.value?.passwordEntered?.let { it1 ->
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
