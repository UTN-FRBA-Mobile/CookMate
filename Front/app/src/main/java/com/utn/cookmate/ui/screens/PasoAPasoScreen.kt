package com.utn.cookmate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
                for (paso in recetaElegida.listaPasos) {
                    TextComponent(paso, textSize = 20.sp)
                    Spacer(modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}