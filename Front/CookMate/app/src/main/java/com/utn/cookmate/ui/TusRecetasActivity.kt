package com.utn.cookmate.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.utn.cookmate.R
import com.utn.cookmate.core.dominio.Receta
import com.utn.cookmate.core.service.RecetasService
import com.utn.cookmate.core.service.RecetasServiceMock
class TusRecetasActivity : AppCompatActivity() {
    private val recetasService: RecetasService = RecetasServiceMock()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tus_recetas)

        // Obtener las recetas mockeadas
        val recetas = recetasService.obtenerRecetas()

        // Obtener la referencia al ListView en el layout
        val listView: ListView = findViewById(R.id.listaRecetas)

        // Crear un adaptador para la lista de recetas y asignarlo al ListView
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, obtenerNombresRecetas(recetas))
        listView.adapter = adapter
    }

    private fun obtenerNombresRecetas(recetas: List<Receta>): List<String> {
        // Obtener los nombres de las recetas para mostrar en la lista
        return recetas.map { it.nombre }
    }
}
