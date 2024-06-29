package com.utn.cookmate.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.connection.Server
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel

@Composable
fun MisRecetasScreen (userInputViewModel: UserInputViewModel, navController : NavController){
    userInputViewModel.appStatus.value.ingredientesElegidos.clear()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val state = rememberScrollState()
        LaunchedEffect(Unit) { state.animateScrollTo(0) }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
            verticalArrangement= Arrangement.SpaceBetween
        ) {
            TopBar(value = "Estas son tus recetas guardadas")
            Spacer(modifier = Modifier.size(30.dp))
            /*SmallFloatingActionButton(
                onClick = { Modifier.clickable { navController.navigate(Routes.GENERAR_RECETA_SCREEN) }},
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Filled.Add, "Small floating action button.")
            }*/
            /*Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Routes.GENERAR_RECETA_SCREEN) }, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Crear receta!!!",
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "\uD83D\uDCA1",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.size(30.dp))*/
            Column(modifier = Modifier.verticalScroll(state).weight(1f, true)){
                for (receta in userInputViewModel.appStatus.value.recetasGuardadas) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = receta.nombre,
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable {
                                userInputViewModel.appStatus.value.recetaElegida = receta
                                navController.navigate(Routes.PASO_A_PASO_SCREEN)
                            }
                        )
                        Spacer(modifier = Modifier
                            .weight(1f)
                            .clickable {
                                userInputViewModel.appStatus.value.recetaElegida = receta
                                navController.navigate(Routes.PASO_A_PASO_SCREEN)
                            })
                        Text(
                            modifier = Modifier.clickable {
                                //AlertDialogExample({ println("ok") },{ println("no") },"TITULO","Seguro?",Icons.Default.Info)
                                userInputViewModel.appStatus.value.recetaElegida = receta
                                navController.navigate(Routes.SEGURO_ELIMINAR_RECETA_SCREEN)
                              },
                            text = "\uD83D\uDDD1\uFE0F",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.Absolute.Right,
                verticalAlignment = Alignment.Bottom
            ) {
                LargeFloatingActionButton(
                    onClick = {
                        Server(userInputViewModel).getAllIngredients()
                        navController.navigate(Routes.BUSCAR_RECETA_ONLINE_SCREEN)
                    },
                    shape = CircleShape,
                ) {
                    Icon(Icons.Filled.Add, "Buscar una receta online")
                }
            }
        }

            //.verticalScroll(state)


    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
