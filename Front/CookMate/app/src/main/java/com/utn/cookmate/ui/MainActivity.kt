package com.utn.cookmate.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController

import com.utn.cookmate.core.entity.Recipe
import com.utn.cookmate.core.entity.Step
import com.utn.cookmate.core.service.RecetasServiceSocket
import com.utn.cookmate.ui.theme.CookMateTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CookMateTheme {
               // val navController = rememberNavController()
                val recetasService = RecetasServiceSocket()
                val recetas = remember { mutableStateOf<List<Recipe>>(emptyList()) }

                val coroutineScope = rememberCoroutineScope()

                // Obtener las recetas desde el servicio
                coroutineScope.launch {
                    recetasService.obtenerRecetas { recetasList ->
                        recetas.value = recetasList
                    }
                }

                /*NavHost(
                    navController = navController,
                    startDestination = "recetas"
                ) {
                    composable("recetas") {
                        TusRecetasScreen(recetas.value) { receta ->
                            navController.navigate("detalle/${receta.nombre}")
                        }
                    }
                    composable("detalle/{nombre}") { backStackEntry ->
                        val nombre = backStackEntry.arguments?.getString("nombre")
                        val receta = recetas.value.find { it.nombre == nombre }
                        receta?.let {
                            DetalleRecetaScreen(receta = it)
                        }
                    }
                }*/
            }
        }
    }
}
