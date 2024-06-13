package com.utn.cookmate.data

data class AppStatus(
    var nameEntered : String = "", //default value empty
    var passwordEntered: String = "",

    var ingredientesElegidos: MutableList<String> = mutableListOf<String>(),

    var recetaElegida : Receta? = null,  //el '?' significa que puede ser null
    var pasoActual: Int = 1,

    var response : String = "",

    var recetasGuardadas : MutableList<Receta> = mutableListOf<Receta>(),
    var recetasEncontradas : MutableList<Receta> = mutableListOf<Receta>()
)