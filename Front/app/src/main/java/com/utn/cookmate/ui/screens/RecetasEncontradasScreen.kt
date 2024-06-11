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
fun RecetasEncontradasScreen (userInputViewModel: UserInputViewModel, navController : NavController){
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
            TopBar(value = "Estas son las recetas encontradas con esos ingredientes")
            Spacer(modifier = Modifier.size(30.dp))
            for (receta in userInputViewModel.appStatus.value.recetasEncontradas) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = receta.nombre,
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { println("click en receta") }
                    )
                    Spacer(modifier = Modifier.weight(1f).clickable { println("click en receta")})
                    Text(
                        modifier = Modifier.clickable { funGuardar(receta.id,userInputViewModel) },
                        text = "\uD83D\uDDAA",
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

fun funGuardar(idAGuardar:String,userInputViewModel: UserInputViewModel){
    for(receta in userInputViewModel.appStatus.value.recetasEncontradas){
        println(receta.id + " vs " + idAGuardar)
        if(receta.id.equals(idAGuardar)){
            receta.guardada = true;
            userInputViewModel.appStatus.value.recetasEncontradas.add(receta)
            return;
        }
    }
}
