package com.utn.cookmate.connection

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.utn.cookmate.ui.UserInputViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
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

    private fun sendSocketRequest(action: String, body: JsonObject) {
        launch(Dispatchers.IO) {
            try {
                val host = "198.199.90.109"
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
                    "login" -> userInputViewModel.appStatus?.value?.loginResponse?.value = response
                    "register" -> userInputViewModel.appStatus?.value?.registerResponse?.value = response
                    "addRecipeToUser" -> userInputViewModel.appStatus?.value?.addRecipeToUserResponse?.value = response
                    "removeRecipeFromUser" -> userInputViewModel.appStatus?.value?.removeRecipeFromUserResponse?.value = response
                    "searchRecipes" -> userInputViewModel.appStatus?.value?.searchRecipesResponse?.value = response
                    "getAllIngredients" -> userInputViewModel.appStatus?.value?.getAllIngredientsResponse?.value = response
                    "downloadResources" -> userInputViewModel.appStatus?.value?.downloadResourcesResponse?.value = response
                }

                // Cerrar conexiones
                inputStream.close()
                outputStream.close()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

}
