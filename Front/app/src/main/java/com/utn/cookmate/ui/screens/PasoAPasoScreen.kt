package com.utn.cookmate.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.data.Receta
import com.utn.cookmate.ui.NormalBar
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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(state)
        ) {
            var recetaElegida = userInputViewModel.appStatus.value.recetaElegida;
            if (recetaElegida != null) {
                TopBar(value = recetaElegida.nombre)
                Spacer(modifier = Modifier.size(20.dp))
                Row(modifier = Modifier.fillMaxWidth()){
                        Button(
                            enabled = userInputViewModel.appStatus.value.pasoActual.value != 1,
                            //modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                userInputViewModel.appStatus.value.pasoActual.value--;
                                //navController.navigate(Routes.PASO_A_PASO_SCREEN)
                            }
                        ) {
                            TextComponent(
                                textValue = "Paso anterior",
                                textSize = 18.sp,
                                colorValue = Color.White
                            )
                        }
                        Button(
                            enabled = userInputViewModel.appStatus.value.pasoActual.value != recetaElegida.listaPasos.size,
                            //modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                userInputViewModel.appStatus.value.pasoActual.value++;
                               // navController.navigate(Routes.PASO_A_PASO_SCREEN)
                            }
                        ) {
                            TextComponent(
                                textValue = "Paso siguiente",
                                textSize = 18.sp,
                                colorValue = Color.White
                            )
                        }
                }
                var paso = recetaElegida.listaPasos.get(userInputViewModel.appStatus.value.pasoActual.value - 1)
                Spacer(modifier = Modifier.size(20.dp))
                NormalBar("Paso " + userInputViewModel.appStatus.value.pasoActual.value)
                Spacer(modifier = Modifier.size(15.dp))
                TextComponent(paso.descripcion, textSize = 20.sp)
                Spacer(modifier = Modifier.size(20.dp))
                Image(
                    bitmap = BitmapFactory.decodeByteArray(paso.image,0,paso.image.size).asImageBitmap(),
                    contentDescription = "contentDescription"
                )
                Spacer(modifier = Modifier.size(20.dp))
                NormalBar("Ingredientes requeridos")
                Row(modifier = Modifier.fillMaxWidth().padding(20.dp),){
                    Column(){
                        if(paso.ingredientes.isNotEmpty()){
                            for (ingrediente in paso.ingredientes) {
                                NormalBar(ingrediente.nombre + " (" + ingrediente.cantidad + ")",ingrediente.image)
                                Spacer(modifier = Modifier.size(50.dp))
                            }
                        } else {
                            NormalBar("Ninguno para este paso!")
                            Spacer(modifier = Modifier.size(20.dp))
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().weight(0.5F).padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            userInputViewModel.appStatus.value.pasoActual.value = 1;
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