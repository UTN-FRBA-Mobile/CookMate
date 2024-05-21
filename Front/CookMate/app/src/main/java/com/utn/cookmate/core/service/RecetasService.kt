package com.utn.cookmate.core.service

import com.utn.cookmate.core.entity.Recipe


// Interfaz que define los endpoints del servicio de recetas
interface RecetasService {
    fun obtenerRecetas(callback: (List<Recipe>) -> Unit)
}
