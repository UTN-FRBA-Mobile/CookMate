package com.utn.cookmate.core.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.utn.cookmate.core.entity.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import kotlin.coroutines.CoroutineContext

class RecetasServiceSocket() : RecetasService, CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun obtenerRecetas(callback: (List<Recipe>) -> Unit) {
        launch {
            val recetas = withContext(Dispatchers.IO) {
                val recetas = mutableListOf<Recipe>()
                try {
                    val host = "0.tcp.sa.ngrok.io"
                    val port = 17736

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
                    // Procesar la lista de recetas
                    val listType = object : TypeToken<List<Recipe>>() {}.type
                    recetas.addAll(gson.fromJson(response, listType))

                    // Cerrar conexiones
                    inputStream.close()
                    outputStream.close()
                    socket.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                recetas
            }
            callback(recetas)
        }
    }

    fun clear() {
        job.cancel()
    }
}
