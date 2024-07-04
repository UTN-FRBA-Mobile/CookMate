import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.utn.cookmate.R
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
            TopBar(value = "Elegí los ingredientes que quieras"/* \uD83D\uDCA1*/)
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

            Column(modifier = Modifier
                .verticalScroll(state)
                .weight(1f, true)) {
                if (ingredientesEnServidor.value.length() > 0) {
                    val ingredients = (0 until ingredientesEnServidor.value.length()).map { i ->
                        ingredientesEnServidor.value.getString(i)
                    }

                    val checkboxColors = CheckboxDefaults.colors(
                        checkedColor = colorResource(id = R.color.purple_700),
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White
                    )

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
                                checked = ingredientesSeleccionados.contains(item),
                                colors = checkboxColors
                            )
                        }
                    } else {
                        // Mostrar ingredientes filtrados, excluyendo los ya seleccionados, cuando hay búsqueda activa
                        val filteredIngredients = ingredients.filter {
                            it.contains(searchQuery, ignoreCase = true) && !ingredientesSeleccionados.contains(it)
                        }

                        val checkboxColors = CheckboxDefaults.colors(
                            checkedColor = colorResource(id = R.color.purple_700),
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )

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
                                checked = ingredientesSeleccionados.contains(item),
                                colors = checkboxColors
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
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                        onClick = {
                            userInputViewModel.appStatus.value?.ingredientesElegidos = ingredientesSeleccionados
                            Server(userInputViewModel).searchRecipes()
                        }
                    ) {
                        TextComponent(
                            textValue = "Buscar recetas",
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
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
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

            if (userInputViewModel.appStatus.value?.recipesSearchAnswered?.value == true) {
                navController.navigate(Routes.RECETAS_ENCONTRADAS_SCREEN)
            }
        }
    }
}

@Composable
fun CheckboxRow(
    text: String,
    onCheckedChange: (Boolean) -> Unit,
    checked: Boolean = false,
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = colors
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
