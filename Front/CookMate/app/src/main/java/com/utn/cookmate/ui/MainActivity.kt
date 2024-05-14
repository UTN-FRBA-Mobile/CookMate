package com.utn.cookmate.ui
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.utn.cookmate.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear un TextView y establecer el texto
        val textView = android.widget.TextView(this)
        textView.text = "Hola Mundo"

        // Establecer el tamaño y el color del texto
        textView.textSize = 24f
        textView.setTextColor(android.graphics.Color.BLACK)

        // Establecer el fondo de la actividad
        textView.setBackgroundColor(android.graphics.Color.parseColor("#FFD9EB"))

        // Establecer el TextView como el contenido de la actividad
        setContentView(textView)
    }
}
