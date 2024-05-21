package com.utn.cookmate.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.utn.cookmate.R
import com.utn.cookmate.core.dominio.Receta
import com.utn.cookmate.core.entity.Recipe
import com.utn.cookmate.core.service.RecetasService
import com.utn.cookmate.core.service.RecetasServiceMock
import com.utn.cookmate.core.service.RecetasServiceSocket

class TusRecetasActivity : AppCompatActivity() {
    val recetasService: RecetasServiceSocket = RecetasServiceSocket()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tus_recetas)

        // Obtener las recetas mockeadas
        recetasService.obtenerRecetas { recetas ->
            // Aquí puedes usar las recetas obtenidas
            mostrarRecetas(recetas)
        }

    }

    private fun mostrarRecetas(recetas: List<Recipe>) {
    // Obtener la referencia al ListView en el layout
        val listView: ListView = findViewById(R.id.listaRecetas)

        // Crear un adaptador para la lista de recetas y asignarlo al ListView
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, obtenerNombresRecetas(recetas))
        listView.adapter = adapter

        val botonBuscaReceta: Button = findViewById(R.id.botonBuscaReceta)
        botonBuscaReceta.setOnClickListener {
            // Intent para abrir la otra actividad (BuscaRecetaActivity)
            val intent = Intent(this, BuscaRecetaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun obtenerNombresRecetas(recetas: List<Recipe>): List<String> {
        // Obtener los nombres de las recetas para mostrar en la lista
        return recetas.map { it.nombre }
    }
}
