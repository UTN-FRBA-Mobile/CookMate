package com.utn.cookmate.core.dominio

data class Ingrediente(
    val nombre: String,
    val cantidad: String,
    val unidad: String
)

data class Paso(
    val numero: Int,
    val descripcion: String,
    val ingredientes: List<Ingrediente>
)

data class Receta(
    val nombre: String,
    val pasos: List<Paso>
)
