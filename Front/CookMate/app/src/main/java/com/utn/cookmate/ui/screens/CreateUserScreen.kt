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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.R
import com.utn.cookmate.data.UserDataUiEvents
import com.utn.cookmate.connection.Server
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TextFieldComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.Base64

@Composable
fun CreateUserScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
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

                when (userInputViewModel.appStatus.value?.registerResult?.value) {
                    "OK" -> {
                        TextComponent(textValue = "Registrado correctamente! Para activar la cuenta, vuelve e inicia sesión por primera vez.", textSize = 12.sp)
                        LaunchedEffect(true) {
                            navController.navigate(Routes.LOGIN_SCREEN) {
                                popUpTo(Routes.CREATE_USER_SCREEN) { inclusive = true }
                            }
                        }
                    }
                    "ERROR" -> {
                        TextComponent(textValue = "El correo elegido ya está registrado u ocurrió un error.", textSize = 12.sp)
                    }
                    null -> {
                        // No mostrar nada hasta que se haga un intento de registro
                    }
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                    onClick = {
                        if (userInputViewModel.isValidRegisterState()) {
                            val success = userInputViewModel.appStatus?.value?.registerEmailEntered?.let {
                                userInputViewModel.appStatus?.value?.registerPasswordEntered?.let { it1 ->
                                    userInputViewModel.appStatus?.value?.registerNameEntered?.let { it2 ->
                                        Server(userInputViewModel)
                                            .register(
                                                it,
                                                it1,
                                                it2
                                            )
                                    }
                                }
                            }
                            //registerSuccess?.value = userInputViewModel.appStatus.value?.registerResult?.value
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                onClick = {
                    navController.navigate(Routes.LOGIN_SCREEN)
                }
            ) {
                TextComponent(textValue = "Volver", textSize = 18.sp, colorValue = Color.White)
            }
        }
    }
}
