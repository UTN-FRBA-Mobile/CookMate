package com.utn.cookmate.core.service

import com.utn.cookmate.core.dominio.Ingrediente
import com.utn.cookmate.core.dominio.Paso
import com.utn.cookmate.core.dominio.Receta

class RecetasServiceMock : RecetasService {
    override fun obtenerRecetas(): List<Receta> {
        // Crea y devuelve una lista de recetas harcodeadas
        return listOf(
            Receta( "Torta de chocolate",
                listOf(
                    Paso(
                        1,
                        "Mezclar los ingredientes",
                        listOf(
                            Ingrediente(
                                "Quinoa",
                                "10",
                                "gramos")
            )))),
            Receta(
                "Ensalada de quinoa",
                listOf(
                    Paso(
                        1,
                        "Cocinar la quinoa",
                        listOf(
                            Ingrediente(
                                "Quinoa",
                                "10",
                                "gramos")
            ))))
        )
    }
}
