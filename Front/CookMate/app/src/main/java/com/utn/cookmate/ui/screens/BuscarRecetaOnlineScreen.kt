import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.connection.Server
import com.utn.cookmate.data.Ingrediente
import com.utn.cookmate.data.Paso
import com.utn.cookmate.data.Receta
import com.utn.cookmate.ui.TextComponent
import com.utn.cookmate.ui.TopBar
import com.utn.cookmate.ui.UserInputViewModel
import com.utn.cookmate.ui.screens.Routes
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerarRecetaScreen(userInputViewModel: UserInputViewModel, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val ingredientesEnServidor = remember { mutableStateOf(JSONArray()) }
    val ingredientesSeleccionados = remember { mutableStateListOf<String>() }

    LaunchedEffect(userInputViewModel.appStatus.value?.getAllIngredientsResponse?.value) {
        val response = userInputViewModel.appStatus.value?.getAllIngredientsResponse?.value
        if (!response.isNullOrEmpty()) {
            ingredientesEnServidor.value = JSONArray(response)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        val state = rememberScrollState()
        LaunchedEffect(Unit) { state.animateScrollTo(0) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TopBar(value = "Buscar recetas con ingredientes dados \uD83D\uDCA1")
            Spacer(modifier = Modifier.size(30.dp))

            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                label = { Text("Buscar Ingrediente") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Column(modifier = Modifier.verticalScroll(state).weight(1f, true)) {
                if (ingredientesEnServidor.value.length() > 0) {
                    val ingredients = (0 until ingredientesEnServidor.value.length()).map { i ->
                        ingredientesEnServidor.value.getString(i)
                    }

                    if (searchQuery.isEmpty()) {
                        // Mostrar todos los ingredientes, incluidos los seleccionados, cuando no hay búsqueda activa
                        ingredients.forEach { item ->
                            CheckboxRow(
                                text = item,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        ingredientesSeleccionados.add(item)
                                    } else {
                                        ingredientesSeleccionados.remove(item)
                                    }
                                },
                                checked = ingredientesSeleccionados.contains(item)
                            )
                        }
                    } else {
                        // Mostrar ingredientes filtrados, excluyendo los ya seleccionados, cuando hay búsqueda activa
                        val filteredIngredients = ingredients.filter {
                            it.contains(searchQuery, ignoreCase = true) && !ingredientesSeleccionados.contains(it)
                        }

                        filteredIngredients.forEach { item ->
                            CheckboxRow(
                                text = item,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        ingredientesSeleccionados.add(item)
                                        searchQuery = "" // Reiniciar la búsqueda
                                    } else {
                                        ingredientesSeleccionados.remove(item)
                                    }
                                },
                                checked = ingredientesSeleccionados.contains(item)
                            )
                        }
                    }
                }
            }

            if (ingredientesSeleccionados.isNotEmpty()) {
                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Dentro de tu función GenerarRecetaScreen
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            userInputViewModel.appStatus.value?.ingredientesElegidos =
                                ingredientesSeleccionados
                            Server(userInputViewModel).searchRecipes()
                            val response = userInputViewModel.appStatus.value?.searchRecipesResponse?.value

                            if (response?.isNotEmpty() == true) {
                                userInputViewModel.appStatus.value?.recetasEncontradas?.clear()

                                val recetasEncontradas = JSONArray(response)
                                for (i in 0 until recetasEncontradas.length()) {
                                    val item = recetasEncontradas.getJSONObject(i)
                                    val nombre = item.getString("nombre")
                                    val listaPasos = item.getJSONArray("pasos")
                                    val listaDePasos = mutableListOf<Paso>()

                                    for (j in 0 until listaPasos.length()) {
                                        val paso = listaPasos.getJSONObject(j)
                                        val numeroPaso = paso.getInt("numero")
                                        val descripcionPaso = paso.getString("descripcion")
                                        val imagen = paso.getString("imagen")
                                        val duracionPaso = if (paso.has("duracion")) paso.getInt("duracion") else null
                                        val listaIngredientes = paso.getJSONArray("ingredientes")
                                        val listaDeIngredientes = mutableListOf<Ingrediente>()

                                        for (k in 0 until listaIngredientes.length()) {
                                            val ingrediente = listaIngredientes.getJSONObject(k)
                                            val nombreIngrediente = ingrediente.getString("nombre")
                                            val cantidad = ingrediente.getInt("cantidad")
                                            val imagenIngrediente = ingrediente.getString("imagen")
                                            val ingredienteObjeto = Ingrediente(nombreIngrediente, cantidad, imagenIngrediente)
                                            listaDeIngredientes.add(ingredienteObjeto)
                                        }

                                        val pasoObjeto = Paso(numeroPaso, descripcionPaso, imagen, listaDeIngredientes, duracionPaso)
                                        listaDePasos.add(pasoObjeto)
                                    }

                                    val receta = Receta(nombre, listaDePasos, false)
                                    userInputViewModel.appStatus.value?.recetasEncontradas?.add(receta)
                                }

                                navController.navigate(Routes.RECETAS_ENCONTRADAS_SCREEN)
                            }
                        }
                    ) {
                        TextComponent(
                            textValue = "Buscar recetas!",
                            textSize = 18.sp,
                            colorValue = Color.White
                        )
                    }

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
                    onClick = {
                        navController.navigate(Routes.MIS_RECETAS_SCREEN)
                    }
                ) {
                    TextComponent(
                        textValue = "Volver",
                        textSize = 18.sp,
                        colorValue = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CheckboxRow(
    text: String,
    onCheckedChange: (Boolean) -> Unit,
    checked: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
