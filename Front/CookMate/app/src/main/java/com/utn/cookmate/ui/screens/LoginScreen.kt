package com.utn.cookmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.JsonObject
import com.utn.cookmate.data.UserDataUiEvents
import com.utn.cookmate.connection.Server
import com.utn.cookmate.data.Ingrediente
import com.utn.cookmate.data.Paso
import com.utn.cookmate.data.Receta

import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TextFieldComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.Base64

@Composable
fun LoginScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
    if(userInputViewModel.appStatus.value.downloadResourcesResponse.value.isEmpty()){
        Server(userInputViewModel).downloadResources()
    } else if(userInputViewModel.appStatus.value.imagenesDescargadas.isEmpty()){
        Thread(Runnable{
            var imagenes : JSONArray = JSONArray(userInputViewModel.appStatus.value.downloadResourcesResponse.value)
            for (i in 0 until imagenes.length()) {
                val item : JSONObject= imagenes.getJSONObject(i)
                val nombre = item.getString("nombre")
                val base64 = item.getString("base64")
                userInputViewModel.appStatus.value.imagenesDescargadas.put(nombre,Base64.getDecoder().decode(base64))
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
                userInputViewModel.appStatus.value.registerResponse.value = ""
                TopBar("CookMate! \uD83D\uDE0A")
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
                /*TextComponent(textValue = "Elegir comida", textSize = 18.sp)
                Spacer(modifier = Modifier.size(10.dp))
                Row(modifier = Modifier.fillMaxWidth()){
                    FoodCard(image = R.drawable.burger,userInputViewModel.uiState.value.foodSelected == "hamburguesa", foodSelected = {
                        userInputViewModel.onEvent(UserDataUiEvents.FoodSelected(it))
                    })
                    FoodCard(image = R.drawable.pizza, foodSelected = {userInputViewModel.onEvent(
                        UserDataUiEvents.FoodSelected(it))}, selected = userInputViewModel.uiState.value.foodSelected == "pizza")
                }
                Spacer(modifier = Modifier.weight(1f))

                 */

//                val originalResponse = remember { mutableStateOf(userInputViewModel.appStatus.value.response) }
//                val theResponse by remember {
//                    mutableStateOf(userInputViewModel.appStatus.value.response)
//                }
                if(userInputViewModel.appStatus.value.loginResponse.value == "{}") {
                    TextComponent(textValue = "Login fallido!", textSize = 12.sp)
                } else if(userInputViewModel.appStatus.value.loginResponse.value != "") {
                    userInputViewModel.appStatus.value.recetasGuardadas.clear()
                    var recetasGuardadas : JSONArray = JSONArray(userInputViewModel.appStatus.value.loginResponse.value)
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
                            //Base64.getDecoder().decode(base64ImagePaso)
                            val listaIngredientes = paso.getJSONArray("ingredientes")
                            var listaDeIngredientes : MutableList<Ingrediente> = mutableListOf<Ingrediente>()
                            for (k in 0 until listaIngredientes.length()) {
                                val ingrediente : JSONObject= listaIngredientes.getJSONObject(k)
                                val nombreIngrediente = ingrediente.getString("nombre")
                                val cantidad = ingrediente.getInt("cantidad")
                                val imagenIngrediente = ingrediente.getString("imagen")
                                var ingredienteObjeto : Ingrediente = Ingrediente(nombreIngrediente,cantidad,imagenIngrediente)
                                listaDeIngredientes.add(ingredienteObjeto)
                            }
                            var pasoObjeto : Paso = Paso(numeroPaso,descripcionPaso,imagen,listaDeIngredientes)
                            listaDePasos.add(pasoObjeto)
                        }
                        var receta : Receta = Receta(nombre, listaDePasos, true)
                        userInputViewModel.appStatus.value.recetasGuardadas.add(receta)
                    }

                    //userInputViewModel.appStatus.value.recetasGuardadas = parseado;
                    navController.navigate(Routes.MIS_RECETAS_SCREEN)
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (userInputViewModel.isValidLoginState() && userInputViewModel.appStatus.value.imagenesDescargadas.isNotEmpty()) {
                            println("${userInputViewModel.appStatus.value.emailEntered} and ${userInputViewModel.appStatus.value.passwordEntered}")
                            Server(userInputViewModel).login(userInputViewModel.appStatus.value.emailEntered,userInputViewModel.appStatus.value.passwordEntered)
                        }

                    }
                ) {
                    TextComponent(
                        textValue = "LOGIN!",
                        textSize = 18.sp,
                        colorValue = Color.White
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
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
