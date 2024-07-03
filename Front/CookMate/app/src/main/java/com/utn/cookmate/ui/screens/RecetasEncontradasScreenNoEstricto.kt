package com.utn.cookmate.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.utn.cookmate.R
import com.utn.cookmate.connection.Server
import com.utn.cookmate.data.Ingrediente
import com.utn.cookmate.data.Paso
import com.utn.cookmate.data.Receta
import com.utn.cookmate.ui.RecetaCard
import com.utn.cookmate.ui.RecetaEnLista
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel
import org.json.JSONArray

@Composable
fun RecetasEncontradasNoEstrictoScreen (userInputViewModel: UserInputViewModel, navController : NavController){
    userInputViewModel.appStatus?.value?.searchRecipesResponse?.value = ""
    userInputViewModel.appStatus?.value?.ingredientesElegidos?.clear()
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
            TopBar(value = "Recetas encontradas")
            Spacer(modifier = Modifier.size(30.dp))
            Column(modifier = Modifier
                .verticalScroll(state)
                .weight(1f, true)){
                for (receta in userInputViewModel.appStatus?.value?.recetasEncontradas!!) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = receta.nombre,
                            color = Color.Black,
                            fontSize = 22.sp,
                            //modifier = Modifier.clickable { println("click en receta") },
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        var yaEstaGuardada = false;
                        for(recetaGuardada in userInputViewModel.appStatus?.value?.recetasGuardadas!!){
                            if(receta.nombre == recetaGuardada.nombre){
                                yaEstaGuardada = true;
                                break;
                            }
                        }
                        if(yaEstaGuardada){
                            Text(
                                //modifier = Modifier.clickable { funGuardar(receta.nombre,userInputViewModel) },
                                text = "\u2714\uD83D\uDCBE",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.weight(1f))
                                Image(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable { funGuardar(receta.nombre, userInputViewModel) },
                                    painter = painterResource(id = R.drawable.download_button),
                                    contentDescription = "Boton de descarga"
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.size(30.dp))
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                    onClick = {
                        navController.navigate(Routes.MIS_RECETAS_SCREEN)
                    }
                ) {
                    TextComponent(textValue = "Volver a mis recetas", textSize = 18.sp,colorValue = Color.White)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                    onClick = {
                        navController.navigate(Routes.BUSCAR_RECETA_ONLINE_SCREEN)
                    }
                ) {
                    TextComponent(textValue = "Buscar otras recetas", textSize = 18.sp,colorValue = Color.White)
                }
            }
        }
    }
}