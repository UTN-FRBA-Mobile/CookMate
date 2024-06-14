package com.utn.cookmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@Composable
fun SeguroEliminarRecetaScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
        ) {
            var recetaElegida = userInputViewModel.appStatus.value.recetaElegida;
            if (recetaElegida != null) {
                TopBar(value = recetaElegida.nombre)
                Spacer(modifier = Modifier.size(30.dp))
                TextComponent("Seguro de eliminar la receta?", textSize = 20.sp)
                Row(modifier = Modifier.fillMaxWidth()){
                    Button(
                        onClick = {
                            funBorrar(recetaElegida.nombre,userInputViewModel)
                            userInputViewModel.appStatus.value.recetaElegida = null
                            navController.navigate(Routes.MIS_RECETAS_SCREEN)
                        }
                    ) {
                        TextComponent(
                            textValue = "Si",
                            textSize = 18.sp,
                            colorValue = Color.White
                        )
                    }
                    Button(
                        onClick = {
                            userInputViewModel.appStatus.value.recetaElegida = null
                            navController.navigate(Routes.MIS_RECETAS_SCREEN)
                        }
                    ) {
                        TextComponent(
                            textValue = "No",
                            textSize = 18.sp,
                            colorValue = Color.White
                        )
                    }
                }
            }
        }
    }
}

fun funBorrar(nombreReceta:String,userInputViewModel: UserInputViewModel){
    Server(userInputViewModel).removeRecipeFromUser(nombreReceta)
    for(receta in userInputViewModel.appStatus.value.recetasGuardadas){
        if(receta.nombre.equals(nombreReceta)){
            receta.guardada = false;
            userInputViewModel.appStatus.value.recetasGuardadas.remove(receta);
            return;
        }
    }
}