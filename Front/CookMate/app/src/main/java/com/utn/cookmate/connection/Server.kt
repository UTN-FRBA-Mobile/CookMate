package com.utn.cookmate.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.Firebase
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.utn.cookmate.data.Ingrediente
import com.utn.cookmate.data.Paso
import com.utn.cookmate.data.Receta
import com.utn.cookmate.ui.UserInputViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket
import java.util.Base64
import kotlin.coroutines.CoroutineContext

class Server(userInputViewModel: UserInputViewModel) : CoroutineScope {

    val userInputViewModel: UserInputViewModel = userInputViewModel

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun downloadResources() {
        var json = JsonObject()
        json.addProperty("action", "downloadResources")
        sendSocketRequest("downloadResources", json)
    }

    fun login(email: String, password: String): Boolean {
        var json = JsonObject()
        json.addProperty("action", "login")
        json.addProperty("email", email)
        json.addProperty("password", password)
        sendSocketRequest("login", json)
        return true
    }

    fun register(email: String, password: String, nombre: String): Boolean {
        var json = JsonObject()
        json.addProperty("action", "register")
        json.addProperty("email", email)
        json.addProperty("password", password)
        json.addProperty("name", nombre)
        sendSocketRequest("register", json)
        return true
    }

    fun searchRecipes() {
        var json = JsonObject()
        var array: JsonArray = JsonArray()
        System.out.println("Buscando recetas con ingredentes: ")
        for (ingrediente in userInputViewModel.appStatus?.value?.ingredientesElegidos!!) {
            array.add(ingrediente)
            System.out.println("Ingrediente "+ingrediente.toString())
        }
        json.addProperty("action", "searchRecipes")
        json.add("ingredientes", array)
        sendSocketRequest("searchRecipes", json)
    }

    fun searchRecipesNonStrict() {
        var json = JsonObject()
        var array: JsonArray = JsonArray()
        System.out.println("Buscando recetas con ingredentes: ")
        for (ingrediente in userInputViewModel.appStatus?.value?.ingredientesElegidos!!) {
            array.add(ingrediente)
            System.out.println("Ingrediente "+ingrediente.toString())
        }
        json.addProperty("action", "searchRecipesNonStrict")
        json.add("ingredientes", array)
        sendSocketRequest("searchRecipesNonStrict", json)
    }

    fun addRecipeToUser(nombreReceta: String) {
        var json = JsonObject()
        json.addProperty("action", "addRecipeToUser")
        json.addProperty("email", userInputViewModel.appStatus?.value?.emailEntered)
        json.addProperty("nombreReceta", nombreReceta)
        sendSocketRequest("addRecipeToUser", json)
    }

    fun removeRecipeFromUser(nombreReceta: String) {
        var json = JsonObject()
        json.addProperty("action", "removeRecipeFromUser")
        json.addProperty("email", userInputViewModel.appStatus?.value?.emailEntered)
        json.addProperty("nombreReceta", nombreReceta)
        sendSocketRequest("removeRecipeFromUser", json)
    }

    fun getAllIngredients() {
        var json = JsonObject()
        json.addProperty("action", "getAllIngredients")
        sendSocketRequest("getAllIngredients", json)
    }

    var isConnectedValue = false //TODO Cuando esta variable este en false, mostrar una notificacion de que no hay internet
    fun checker(){
        launch(Dispatchers.IO) {
            while(true) {
                isConnected()
                delay(5000)
            }
        }.start()
    }

    fun isConnected(): Boolean {
        val host = "www.google.com"
        val port = 80
        try{
            val socket = Socket(host, port)
            isConnectedValue = socket.isConnected
            socket.close()
        } catch (e: Exception) {
            // e.printStackTrace()
        }
        return isConnectedValue
    }

    private fun sendSocketRequest(action: String, body: JsonObject) {
        launch(Dispatchers.IO) {
            if(isConnected()){
                try {
                    val host = "192.168.0.224"
                    // server matt val host = "198.199.90.109"
                    val port = 9090
                    val socket = Socket(host, port)
                    val outputStream = ObjectOutputStream(socket.getOutputStream())
                    val inputStream = ObjectInputStream(socket.getInputStream())

                    // Enviar solicitud al servidor
                    outputStream.writeObject(body.toString())
                    outputStream.flush()

                    // Leer respuesta del servidor
                    val response = inputStream.readObject().toString()
                    println("RESPONSE: $response" + "\nAccion "+action)

                    // Actualizar el estado de la vista basado en la acción
                    when (action) {
                        "login" -> {
                            userInputViewModel.appStatus?.value?.loginResponse?.value = response
                        }
                        "register" -> processRegisterNewUserResponse(response)
                        "addRecipeToUser" -> {
                            userInputViewModel.appStatus?.value?.addRecipeToUserResponse?.value = response
                        }
                        "removeRecipeFromUser" -> {
                            userInputViewModel.appStatus?.value?.removeRecipeFromUserResponse?.value = response
                        }
                        "searchRecipes" -> processSearchRecipesResponse(response)
                        "searchRecipesNonStrict" -> processSearchRecipesResponse(response)
                        "getAllIngredients" -> {
                            userInputViewModel.appStatus?.value?.getAllIngredientsResponse?.value = response
                        }
                        "downloadResources" -> processDownloadResourcesResponse(response)
                        "downloadImage" -> processDownloadImageResponse(response)
                    }

                    // Cerrar conexiones
                    inputStream.close()
                    outputStream.close()
                    socket.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    fun processRegisterNewUserResponse(response:String){
        if(response == "true"){
            userInputViewModel.appStatus.value?.registerResult?.value = "OK"
        } else {
            userInputViewModel.appStatus.value?.registerResult?.value = "ERROR"
        }
    }

    fun processSearchRecipesResponse(response:String){
        userInputViewModel.appStatus.value?.recetasEncontradas?.clear()
        val recetasEncontradas = JSONArray(response)
        for (i in 0 until recetasEncontradas.length()) {
            val item = recetasEncontradas.getJSONObject(i)
            val nombre = item.getString("nombre")
            val listaPasos = item.getJSONArray("pasos")
            val listaDePasos = mutableListOf<Paso>()

            for (j in 0 until listaPasos.length()) {
                val paso = listaPasos.getJSONObject(j)
                val numeroPaso = paso.getInt("numero")
                val descripcionPaso = paso.getString("descripcion")
                val imagen = paso.getString("imagen")
                val duracionPaso = if (paso.has("duracion")) paso.getInt("duracion") else null
                val listaIngredientes = paso.getJSONArray("ingredientes")
                val listaDeIngredientes = mutableListOf<Ingrediente>()

                for (k in 0 until listaIngredientes.length()) {
                    val ingrediente = listaIngredientes.getJSONObject(k)
                    val nombreIngrediente = ingrediente.getString("nombre")
                    val cantidad = ingrediente.getInt("cantidad")
                    val imagenIngrediente = ingrediente.getString("imagen")
                    val ingredienteObjeto = Ingrediente(nombreIngrediente, cantidad, imagenIngrediente)
                    listaDeIngredientes.add(ingredienteObjeto)
                }

                val pasoObjeto = Paso(numeroPaso, descripcionPaso, imagen, listaDeIngredientes, duracionPaso)
                listaDePasos.add(pasoObjeto)
            }

            val receta = Receta(nombre, listaDePasos, false)
            userInputViewModel.appStatus.value?.recetasEncontradas?.add(receta)
        }
        userInputViewModel.appStatus.value?.recipesSearchAnswered?.value = true
    }

    fun processDownloadResourcesResponse(response: String){
        Thread(Runnable{
            var imagenes : JSONArray = JSONArray(response)
            for (i in 0 until imagenes.length()) {
                val item : JSONObject = imagenes.getJSONObject(i)
                val nombre = item.getString("nombre")
               // val base64 = item.getString("base64")

                var json = JsonObject()
                json.addProperty("action", "downloadImage")
                json.addProperty("imagen", nombre)
                sendSocketRequest("downloadImage", json)

//                userInputViewModel.appStatus?.value?.imagenesDescargadas!!.put(nombre,
//                    Base64.getDecoder().decode(base64))
            }
        }).start()
    }

    fun processDownloadImageResponse(response: String){
            var imagen : JSONObject = JSONObject(response)
                val nombre = imagen.getString("nombre")
                val base64 = imagen.getString("base64")
                userInputViewModel.appStatus?.value?.imagenesDescargadas!!.put(nombre,
                    Base64.getDecoder().decode(base64))
    }

}
