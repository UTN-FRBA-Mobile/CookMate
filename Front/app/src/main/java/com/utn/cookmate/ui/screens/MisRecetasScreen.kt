package com.utn.cookmate.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.data.Receta
import com.utn.cookmate.ui.RecetaCard
import com.utn.cookmate.ui.RecetaEnLista
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel

@Composable
fun MisRecetasScreen (userInputViewModel: UserInputViewModel, navController : NavController){
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
            TopBar(value = "Estas son tus recetas guardadas")
            Spacer(modifier = Modifier.size(30.dp))
            Row(modifier = Modifier.fillMaxWidth().clickable { navController.navigate(Routes.GENERAR_RECETA_SCREEN)}, verticalAlignment = Alignment.CenterVertically) {
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
            Spacer(modifier = Modifier.size(30.dp))
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
                    Spacer(modifier = Modifier.weight(1f).clickable {
                        userInputViewModel.appStatus.value.recetaElegida = receta
                        navController.navigate(Routes.PASO_A_PASO_SCREEN)})
                    Text(
                        modifier = Modifier.clickable { funBorrar(idABorrar = receta.nombre, userInputViewModel) },
                        text = "\uD83D\uDDD1\uFE0F",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.size(30.dp))
            }
        }
    }
}

fun funBorrar(idABorrar:String, userInputViewModel:UserInputViewModel){
    for(receta in userInputViewModel.appStatus.value.recetasGuardadas){
        println(receta.nombre + " vs " + idABorrar)
        if(receta.nombre.equals(idABorrar)){
            userInputViewModel.appStatus.value.recetasGuardadas.remove(receta);
            return;
        }
    }
}