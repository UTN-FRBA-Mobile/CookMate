package com.utn.cookmate.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utn.cookmate.R

class RecetasEncontradasActivity : AppCompatActivity(), AdapterRecetasEncontradas.ItemClickListener {

    private lateinit var adapter: AdapterRecetasEncontradas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas_encontradas)

        //TODO Cargar lista

        // Datos de ejemplo
        val data = listOf("Item 1", "Item 2", "Item 3", "Item 4")

        val recyclerView: RecyclerView = findViewById(R.id.recetas_encontradas_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AdapterRecetasEncontradas(this, data)
        adapter.setClickListener(this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(item: String) {
        // TODO Logica guardar receta
        Toast.makeText(this, "Clicked: $item", Toast.LENGTH_SHORT).show()
    }
}
