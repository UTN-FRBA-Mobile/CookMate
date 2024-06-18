package com.utn.cookmate.ui.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.utn.cookmate.core.entity.Recipe
import com.utn.cookmate.core.service.RecetasServiceSocket

class TusRecetasActivity : ComponentActivity() {
    private val recetasService: RecetasServiceSocket = RecetasServiceSocket()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recetasService.obtenerRecetas { recetas ->
            setContent {
                TusRecetasScreen(recetas = recetas, onRecetaClick = { receta ->
                    val intent = Intent(this, DetalleRecetaActivity::class.java).apply {
                        putExtra("receta", receta)
                    }
                    startActivity(intent)
                })
            }
        }
    }
}

@Composable
fun TusRecetasScreen(recetas: List<Recipe>, onRecetaClick: (Recipe) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Tus Recetas",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(recetas) { receta ->
                Text(
                    text = receta.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { onRecetaClick(receta) }
                )
            }
        }

        Button(
            onClick = { /* Navegar a otra actividad de búsqueda de recetas */ },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Encontrar tu receta")
        }
    }
}
