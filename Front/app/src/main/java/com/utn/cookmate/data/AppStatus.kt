package com.utn.cookmate.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

data class AppStatus(
    var emailEntered: String = "", //default value empty
    var passwordEntered: String = "",

    var ingredientesElegidos: MutableList<String> = mutableStateListOf<String>(),

    var recetaElegida: Receta? = null,  //el '?' significa que puede ser null
    var pasoActual: MutableState<Int> = mutableStateOf(1),

    //var response: String = "",
    var loginResponse: MutableState<String> = mutableStateOf(""),
    var addRecipeToUserResponse: MutableState<String> = mutableStateOf(""),
    var removeRecipeFromUserResponse: MutableState<String> = mutableStateOf(""),
    var searchRecipesResponse: MutableState<String> = mutableStateOf(""),
    var getAllIngredientsResponse: MutableState<String> = mutableStateOf(""),

    var recetasGuardadas: MutableList<Receta> = mutableStateListOf<Receta>(),
    var recetasEncontradas: MutableList<Receta> = mutableListOf<Receta>()


)