package com.utn.cookmate.core.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.utn.cookmate.core.entity.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class RecetasServiceSocket() : RecetasService {

    private var socket: Socket? = null

    override fun obtenerRecetas(callback: (List<Recipe>) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val recetas = withContext(Dispatchers.IO) {
                // Realiza la operación de red aquí
                val recetas = mutableListOf<Recipe>()
                try {
                    val host = "0.tcp.sa.ngrok.io"
                    val port = 19257

                    val socket = Socket(host, port)
                    val outputStream = ObjectOutputStream(socket.getOutputStream())
                    val inputStream = ObjectInputStream(socket.getInputStream())

                    // Enviar solicitud al servidor
                    outputStream.writeObject("obtenerRecetas")
                    outputStream.flush()
                    val gson = Gson()

                    // Leer respuesta del servidor
                    val response = inputStream.readObject().toString()
                    println("response: $response")
                    if (response != null) {
                        // Procesar la lista de recetas
                        val listType = object : TypeToken<List<Recipe>>() {}.type
                        recetas.addAll(gson.fromJson(response, listType))
                    }

                    // Cerrar conexiones
                    inputStream.close()
                    outputStream.close()
                    socket.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                recetas
            }
            // Llama al callback con el resultado
            callback(recetas)
        }
    }

}
