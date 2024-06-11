package com.utn.cookmate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utn.cookmate.R
import com.utn.cookmate.data.UserDataUiEvents
import com.utn.cookmate.ui.ButtonComponent
import com.utn.cookmate.ui.FoodCard
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TextFieldComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel

@Composable
fun UserInputScreen(userInputViewModel: UserInputViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
        ) {
            TopBar("Hi there \uD83D\uDE0A")
            TextComponent(textValue = "choose one", textSize = 24.sp)
            Spacer(modifier = Modifier.size(20.dp))
            TextComponent(textValue = "detalles extra...", textSize = 18.sp)
            Spacer(modifier = Modifier.size(60.dp))
            TextComponent(textValue = "Nombre", textSize = 12.sp)
            TextFieldComponent("Example",onTextChanged = {userInputViewModel.onEvent(UserDataUiEvents.UserNameEntered(it))})
            Spacer(modifier = Modifier.size(20.dp))
            TextComponent(textValue = "Elegir comida", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))
//            Row(modifier = Modifier.fillMaxWidth()){
//                FoodCard(image = R.drawable.burger,userInputViewModel.uiState.value.foodSelected == "hamburguesa", foodSelected = {
//                    userInputViewModel.onEvent(UserDataUiEvents.FoodSelected(it))
//                })
//                FoodCard(image = R.drawable.pizza, foodSelected = {userInputViewModel.onEvent(UserDataUiEvents.FoodSelected(it))}, selected = userInputViewModel.uiState.value.foodSelected == "pizza")
//            }
//            Spacer(modifier = Modifier.weight(1f))
//            if(userInputViewModel.isValidState()) {
//                ButtonComponent(
//                    goToDetailsScreen = {
//                        println("COMING HERE")
//                        println("${userInputViewModel.uiState.value.nameEntered} and ${userInputViewModel.uiState.value.foodSelected}")
//                    }
//                )
//            }
        }
    }
}

//@Preview
//@Composable
//fun UserInputScreenPreview(){
//    UserInputScreen(userInputViewModel)
//}