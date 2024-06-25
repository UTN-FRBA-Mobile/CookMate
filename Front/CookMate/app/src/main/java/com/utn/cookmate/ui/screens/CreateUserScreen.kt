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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
fun CreateUserScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
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
                TopBar("Registrarse en CookMate")
                TextComponent(textValue = "Email", textSize = 12.sp)
                TextFieldComponent(
                    "Email",
                    onTextChanged = { userInputViewModel.onEvent(UserDataUiEvents.RegisterEmailEntered(it)) })
                Spacer(modifier = Modifier.size(20.dp))
                TextComponent(textValue = "Clave", textSize = 12.sp)
                TextFieldComponent(
                    "Clave",
                    onTextChanged = { userInputViewModel.onEvent(UserDataUiEvents.RegisterPasswordEntered(it)) })
                Spacer(modifier = Modifier.size(20.dp))
                TextComponent(textValue = "Nombre", textSize = 12.sp)
                TextFieldComponent(
                    "Nombre",
                    onTextChanged = { userInputViewModel.onEvent(UserDataUiEvents.RegisterNameEntered(it)) })
                Spacer(modifier = Modifier.size(40.dp))
                if(userInputViewModel.appStatus.value.registerResponse.value == "{}") {
                    TextComponent(textValue = "El correo elegido ya esta registrado", textSize = 12.sp)
                } else if(userInputViewModel.appStatus.value.registerResponse.value != "") {
                    TextComponent(textValue = "Registrado correctamente! Para activar la cuenta volver e iniciar sesion por primera vez.", textSize = 12.sp)
                    //navController.navigate(Routes.MIS_RECETAS_SCREEN)
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (userInputViewModel.isValidRegisterState()) {
                            Server(userInputViewModel).register(userInputViewModel.appStatus.value.registerEmailEntered,userInputViewModel.appStatus.value.registerPasswordEntered,userInputViewModel.appStatus.value.registerNameEntered)
                        }
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
            modifier = Modifier.fillMaxWidth().padding(15.dp),//.weight(0.5F).padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate(Routes.LOGIN_SCREEN)
                }
            ) {
                TextComponent(textValue = "Volver", textSize = 18.sp,colorValue = Color.White)
            }
        }

    }
}
