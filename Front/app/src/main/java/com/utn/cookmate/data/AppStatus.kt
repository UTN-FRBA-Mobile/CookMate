package com.utn.cookmate.data

import com.utn.cookmate.ui.Server

data class AppStatus(
    var server: Server = Server(),

    var nameEntered : String = "", //default value empty
    var passwordEntered: String = "",

    var ingredientesElegidos: MutableList<String> = mutableListOf<String>(),

    var recetaElegida : Receta? = null,  //el '?' significa que puede ser null

    var recetasGuardadas : MutableList<Receta> = server.mockRecetas(),
    var recetasEncontradas : MutableList<Receta> = server.mockRecetasEncontradas()
)