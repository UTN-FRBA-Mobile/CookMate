package com.utn.cookmate.core.service

import com.utn.cookmate.core.dominio.Receta


// Interfaz que define los endpoints del servicio de recetas
interface RecetasService {
    fun obtenerRecetas(): List<Receta>
}
