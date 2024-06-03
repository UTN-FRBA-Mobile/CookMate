package com.utn.cookmate.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.utn.cookmate.R
import com.utn.cookmate.core.entity.Recipe

class DetalleRecetaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_receta)

        val nombreRecetaTextView: TextView = findViewById(R.id.nombreReceta)
        val stepsContainer: LinearLayout = findViewById(R.id.stepsContainer)

        // Obtener la receta del Intent
        val receta: Recipe? = intent.getSerializableExtra("receta") as? Recipe

        receta?.let {
            nombreRecetaTextView.text = it.nombre

            for (step in it.pasos) {
                // Crear una vista para cada paso
                val stepDescriptionTextView = TextView(this)
                stepDescriptionTextView.text = getString(R.string.step_description, step.numero, step.descripcion)
                stepDescriptionTextView.textSize = 18f
                stepDescriptionTextView.setTextColor(resources.getColor(android.R.color.black, null))
                stepsContainer.addView(stepDescriptionTextView)

                // Crear una vista para cada ingrediente del paso
                for (ingredient in step.ingredientes) {
                    val ingredientTextView = TextView(this)
                    ingredientTextView.text = getString(R.string.ingredient, ingredient.nombre, ingredient.cantidad)
                    ingredientTextView.textSize = 16f
                    ingredientTextView.setTextColor(resources.getColor(android.R.color.black, null))
                    stepsContainer.addView(ingredientTextView)
                }
            }
        }
    }
}
