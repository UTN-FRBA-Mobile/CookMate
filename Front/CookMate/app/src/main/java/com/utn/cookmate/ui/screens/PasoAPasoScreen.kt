package com.utn.cookmate.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.data.Paso
import com.utn.cookmate.ui.NormalBar
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun PasoAPasoScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
    val appStatus by userInputViewModel.appStatus.observeAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        val state = rememberScrollState()
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(state)
        ) {
            appStatus?.let { status ->
                val pasoActual = status.pasoActual?.value ?: 1
                val recetaElegida = status.recetaElegida

                recetaElegida?.let { receta ->
                    TopBar(value = receta.nombre)
                    Spacer(modifier = Modifier.size(20.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            enabled = pasoActual != 1,
                            onClick = {
                                userInputViewModel.appStatus?.value?.pasoActual?.value = pasoActual - 1
                            }
                        ) {
                            TextComponent(
                                textValue = "Paso anterior",
                                textSize = 18.sp,
                                colorValue = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            enabled = pasoActual != receta.listaPasos.size,
                            onClick = {
                                userInputViewModel.appStatus?.value?.pasoActual?.value = pasoActual + 1
                            }
                        ) {
                            TextComponent(
                                textValue = "Paso siguiente",
                                textSize = 18.sp,
                                colorValue = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    val paso = receta.listaPasos.getOrNull(pasoActual - 1)

                    paso?.let {
                        Spacer(modifier = Modifier.size(20.dp))
                        NormalBar("Paso $pasoActual")
                        Spacer(modifier = Modifier.size(15.dp))
                        TextComponent(it.descripcion, textSize = 20.sp)
                        Spacer(modifier = Modifier.size(20.dp))

                        it.imagen?.let { imagen ->
                            userInputViewModel.appStatus?.value?.imagenesDescargadas?.get(imagen)?.let { imageData ->
                                Image(
                                    bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size).asImageBitmap(),
                                    contentDescription = "Descripción de contenido"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(20.dp))
                        NormalBar("Ingredientes requeridos")
                        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                            if (it.ingredientes.isNotEmpty()) {
                                it.ingredientes.forEach { ingrediente ->
                                    ingrediente.imagen?.let { imagen ->
                                        NormalBar(ingrediente.nombre + " (" + ingrediente.cantidad + ")", userInputViewModel.appStatus?.value?.imagenesDescargadas?.get(imagen))
                                        Spacer(modifier = Modifier.size(20.dp))
                                    }
                                }
                            } else {
                                NormalBar("Ninguno para este paso!")
                                Spacer(modifier = Modifier.size(20.dp))
                            }
                        }

                        // Temporizador
                        it.duracion?.let { duracion ->
                            Temporizador(
                                duracion = duracion,
                                onTimerFinished = {
                                    userInputViewModel.appStatus?.value?.pasoActual?.value = pasoActual + 1
                                }
                            )
                        }

                        Spacer(modifier = Modifier.size(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    userInputViewModel.appStatus?.value?.pasoActual?.value = 1
                                    navController.navigate(Routes.MIS_RECETAS_SCREEN)
                                }
                            ) {
                                TextComponent(
                                    textValue = "Volver",
                                    textSize = 18.sp,
                                    colorValue = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Temporizador(
    duracion: Int,
    onTimerFinished: () -> Unit
) {
    var timerSeconds by remember { mutableStateOf(duracion * 60) }
    var isTimerRunning by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { isTimerRunning = !isTimerRunning }
        ) {
            TextComponent(
                textValue = if (isTimerRunning) "Pausar" else "Iniciar temporizador",
                textSize = 18.sp,
                colorValue = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LaunchedEffect(isTimerRunning) {
            while (isTimerRunning && timerSeconds > 0) {
                delay(1000)
                timerSeconds--
            }
            if (timerSeconds == 0) {
                isTimerRunning = false
                onTimerFinished()
            }
        }
        TextComponent(
            textValue = String.format("%02d:%02d", timerSeconds / 60, timerSeconds % 60),
            textSize = 24.sp,
            colorValue = MaterialTheme.colorScheme.onSurface
        )
    }
}

