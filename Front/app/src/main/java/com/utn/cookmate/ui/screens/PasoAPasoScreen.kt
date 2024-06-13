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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.data.Receta
import com.utn.cookmate.ui.RecetaEnLista
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel

@Composable
fun PasoAPasoScreen (userInputViewModel: UserInputViewModel, navController : NavController){
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val state = rememberScrollState()
        LaunchedEffect(Unit) { state.animateScrollTo(100) }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(state)
        ) {
            var recetaElegida = userInputViewModel.appStatus.value.recetaElegida;
            if (recetaElegida != null) {
                TopBar(value = recetaElegida.nombre)
                Spacer(modifier = Modifier.size(30.dp))
                TextComponent("Paso " + userInputViewModel.appStatus.value.pasoActual, textSize = 20.sp)
                Row(modifier = Modifier.fillMaxWidth()){
                        Button(
                            enabled = userInputViewModel.appStatus.value.pasoActual != 1,
                            //modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                userInputViewModel.appStatus.value.pasoActual--;
                                navController.navigate(Routes.PASO_A_PASO_SCREEN)
                            }
                        ) {
                            TextComponent(
                                textValue = "Paso anterior",
                                textSize = 18.sp,
                                colorValue = Color.White
                            )
                        }
                        Button(
                            enabled = userInputViewModel.appStatus.value.pasoActual != recetaElegida.listaPasos.size,
                            //modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                userInputViewModel.appStatus.value.pasoActual++;
                                navController.navigate(Routes.PASO_A_PASO_SCREEN)
                            }
                        ) {
                            TextComponent(
                                textValue = "Paso siguiente",
                                textSize = 18.sp,
                                colorValue = Color.White
                            )
                        }
                }
                var paso = recetaElegida.listaPasos.get(userInputViewModel.appStatus.value.pasoActual - 1)
                TextComponent(paso, textSize = 20.sp)
                Spacer(modifier = Modifier.size(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().weight(0.5F).padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            userInputViewModel.appStatus.value.pasoActual = 1;
                            navController.navigate(Routes.MIS_RECETAS_SCREEN)
                        }
                    ) {
                        TextComponent(
                            textValue = "Volver",
                            textSize = 18.sp,
                            colorValue = Color.White
                        )
                    }
                }
            }
        }
    }
}