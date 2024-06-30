package com.utn.cookmate.data

data class Paso(
    val numero: Int,
    val descripcion: String,
    val imagen: String,
    val ingredientes: List<Ingrediente>,
    val duracion: Int? = null // Duración en minutos, opcional
)
