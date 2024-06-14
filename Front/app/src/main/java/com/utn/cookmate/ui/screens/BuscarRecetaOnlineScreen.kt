package com.utn.cookmate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.data.UserDataUiEvents
import com.utn.cookmate.connection.Server
import com.utn.cookmate.data.Ingrediente
import com.utn.cookmate.data.Paso
import com.utn.cookmate.data.Receta
import com.utn.cookmate.ui.CheckboxRow
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TextFieldComponent
import com.utn.cookmate.ui.UserInputViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.Base64

@Composable
fun GenerarRecetaScreen (userInputViewModel: UserInputViewModel, navController : NavController){
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
        ) {
            Spacer(modifier = Modifier.size(40.dp))
            TextComponent(textValue = "Buscar recetas con ingredientes dados \uD83D\uDCA1", textSize = 24.sp)
            Spacer(modifier = Modifier.size(20.dp))

            var ingredientesEnServidor : JSONArray = JSONArray(userInputViewModel.appStatus.value.getAllIngredientsResponse.value)
            for (i in 0 until ingredientesEnServidor.length()) {
                val item : String = ingredientesEnServidor.getString(i)
//                TextComponent(textValue = item, textSize = 16.sp)
//                Spacer(modifier = Modifier.size(20.dp))
                CheckboxRow(item,{
                        if(userInputViewModel.appStatus.value.ingredientesElegidos.contains(item)){
                            userInputViewModel.appStatus.value.ingredientesElegidos.remove(item)
                        } else {
                            userInputViewModel.appStatus.value.ingredientesElegidos.add(item)
                        }
                    }
                )
            }
            if(userInputViewModel.isValidBusquedaRecetasState()){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        //Server(userInputViewModel).mockRecetasEncontradas()
                        println("Se buscan recetas con estos ingredientes")
                        Server(userInputViewModel).searchRecipes()
                    }
                ) {
                    TextComponent(textValue = "Buscar recetas!", textSize = 18.sp,colorValue = Color.White)
                }
            }
            if(userInputViewModel.appStatus.value.searchRecipesResponse.value.isNotEmpty()){

                userInputViewModel.appStatus.value.recetasEncontradas.clear()
                var recetasEncontradas : JSONArray = JSONArray(userInputViewModel.appStatus.value.searchRecipesResponse.value)
                for (i in 0 until recetasEncontradas.length()) {
                    var listaDePasos : MutableList<Paso> = mutableListOf<Paso>()
                    val item : JSONObject= recetasEncontradas.getJSONObject(i)
                    val nombre = item.getString("nombre")
                    val listaPasos = item.getJSONArray("pasos")
                    for (j in 0 until listaPasos.length()) {
                        val paso : JSONObject= listaPasos.getJSONObject(j)
                        val numeroPaso = paso.getInt("numero")
                        val descripcionPaso = paso.getString("descripcion")
                        val base64ImagePaso = paso.getString("base64Image")
                        val listaIngredientes = paso.getJSONArray("ingredientes")
                        var listaDeIngredientes : MutableList<Ingrediente> = mutableListOf<Ingrediente>()
                        for (k in 0 until listaIngredientes.length()) {
                            val ingrediente : JSONObject= listaIngredientes.getJSONObject(k)
                            val nombreIngrediente = ingrediente.getString("nombre")
                            val cantidad = ingrediente.getInt("cantidad")
                            val base64Image = ingrediente.getString("base64Image")
                            var ingredienteObjeto : Ingrediente = Ingrediente(nombreIngrediente,cantidad,
                                Base64.getDecoder().decode(base64Image))
                            listaDeIngredientes.add(ingredienteObjeto)
                        }
                        var pasoObjeto : Paso = Paso(numeroPaso,descripcionPaso,
                        Base64.getDecoder().decode(base64ImagePaso),listaDeIngredientes)
                        listaDePasos.add(pasoObjeto)
                    }
                    var receta : Receta = Receta(nombre, listaDePasos, false)
                    userInputViewModel.appStatus.value.recetasEncontradas.add(receta)
                }
                navController.navigate(Routes.RECETAS_ENCONTRADAS_SCREEN)
            }
        }
    }
}