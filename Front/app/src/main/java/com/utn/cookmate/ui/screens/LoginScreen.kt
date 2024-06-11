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
import com.utn.cookmate.R
import com.utn.cookmate.data.UserDataUiEvents
import com.utn.cookmate.ui.ButtonComponent
import com.utn.cookmate.ui.FoodCard
import com.utn.cookmate.ui.Server
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TextFieldComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel

@Composable
fun LoginScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                TopBar("Hi there \uD83D\uDE0A")
                TextComponent(textValue = "Nombre", textSize = 12.sp)
                TextFieldComponent(
                    "Nombre",
                    onTextChanged = { userInputViewModel.onEvent(UserDataUiEvents.UserNameEntered(it)) })
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
                if (userInputViewModel.isValidLoginState()) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            println("${userInputViewModel.appStatus.value.nameEntered} and ${userInputViewModel.appStatus.value.passwordEntered}")
                            if (userInputViewModel.appStatus.value.server.login()) {
                                navController.navigate(Routes.MIS_RECETAS_SCREEN)
                            } else {
                                navController.navigate(Routes.LOGIN_SCREEN)
                            }
                        }
                    ) {
                        TextComponent(
                            textValue = "LOGIN!",
                            textSize = 18.sp,
                            colorValue = Color.White
                        )
                    }
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