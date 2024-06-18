package com.utn.cookmate.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utn.cookmate.core.entity.Recipe

class DetalleRecetaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receta = intent.getSerializableExtra("receta") as? Recipe

        setContent {
            receta?.let {
                DetalleRecetaScreen(receta = it)
            }
        }
    }
}

@Composable
fun DetalleRecetaScreen(receta: Recipe) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = receta.nombre,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        receta.pasos.forEach { step ->
            Text(
                text = "Paso ${step.numero}: ${step.descripcion}",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            step.ingredientes.forEach { ingredient ->
                Text(
                    text = "${ingredient.nombre}: ${ingredient.cantidad}",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                )
            }
        }
    }
}
