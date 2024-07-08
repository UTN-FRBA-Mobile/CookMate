package com.utn.cookmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.R
import com.utn.cookmate.ui.TextComponent


var integrantes : List<String> = listOf("Pablo Matías Pardo","Agustin De Cesare","Federico Hedderwick","Iván De Luca")

@Composable
fun AboutScreen(navController: NavController){
    Surface(
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.fillMaxSize().padding(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "CookMate v2.0")
            }
            Row(
                modifier = Modifier.fillMaxWidth().weight(0.75F),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    for(integrante in integrantes) {
                        Spacer(modifier = Modifier.size(40.dp))
                        Text(text = integrante)
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
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                    onClick = {
                        navController.navigate(Routes.LOGIN_SCREEN)
                    }
                ) {
                    TextComponent(textValue = "Volver", textSize = 18.sp,colorValue = Color.White)
                }
            }
        }
    }
}