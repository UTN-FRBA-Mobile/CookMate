package com.utn.cookmate.connection

import com.google.gson.JsonObject
import com.utn.cookmate.data.Receta
import com.utn.cookmate.ui.UserInputViewModel
import io.socket.client.IO
import io.socket.client.Socket
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Server(userInputViewModel : UserInputViewModel) {
    val userInputViewModel : UserInputViewModel = userInputViewModel

    private val socket: Socket = IO.socket("http://localhost:9090")
    fun connect() {
        socket.connect()

        socket.on(Socket.EVENT_CONNECT) {
            println("Connected to server")
            socket.emit("chat message", "hola")
        }

        socket.on("chat message") { args ->
            val message = args[0] as String
            println("Received message: $message")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            println("Disconnected from server")
        }
    }

    fun sendMessage(message: String) {
        socket.emit("chat message", message)
    }

    fun disconnect() {
        socket.disconnect()
    }


    fun login(username:String, password:String) : Boolean{
        var json = JsonObject()
        json.addProperty("action","login")
        json.addProperty("username",username)
        json.addProperty("password",password)
        var apiResponse = sendPost(json)
//        val service = PostApi.instance
//
//        connect()
//        Thread.sleep(1000) // Give some time for connection to establish
//        sendMessage("Hello, everyone!")
//        Thread.sleep(3000) // Allow time to receive messages
//        disconnect()
        //TODO consultar al server
        return true;
    }

    fun mockRecetasEncontradas(): MutableList<Receta> {
        val list: MutableList<Receta> = mutableListOf()
        list.add(Receta("1","Pollo",mutableListOf<String>("Cortale las alas","Ahora metelo al horno")))
        list.add(Receta("2","Milanesa",mutableListOf<String>("Hacer la milanesa", "Metela al horno")))
        list.add(Receta("14","Ensalada",mutableListOf<String>("Mezcla verduras","Poneles aceite")))
        list.add(Receta("15","Pascualina",mutableListOf<String>("Llena una masa con acelga","ponela al horno")))
        list.add(Receta("16","Empanadas",mutableListOf<String>("Rellena tapas de empanada","cocinalas")))
        return list;
    }

    fun mockRecetas(): MutableList<Receta> {
        val list: MutableList<Receta> = mutableListOf()
        list.add(Receta("1","Pollo",mutableListOf<String>("Cortale las alas","Ahora metelo al horno"),true))
        list.add(Receta("2","Milanesa",mutableListOf<String>("Hacer la milanesa", "Metela al horno"),true))
        list.add(Receta("3","Pizza",mutableListOf<String>("Prepara la masa", "Ponele salsa", "Ponele queso", "Poner en el horno"),true))
        list.add(Receta("4","Matambre",mutableListOf<String>("Comprar el matambre", "Cocinarlo"),true))
        list.add(Receta("5","Huevo frito",mutableListOf<String>("Romper un huevo en la sarten", "Poner la sarten al fuego"),true))
        list.add(Receta("6","Hamburguesa",mutableListOf<String>("Llamar a McDonald", "Pedir un Big Mac"),true))
        list.add(Receta("7","Merluza",mutableListOf<String>("Empanar el pescado","Ponerlo al horno"),true))
        list.add(Receta("8","Picada",mutableListOf<String>("Cortar los fiambres en cubitos","Servirlo en bandeja"),true))
        list.add(Receta("9","Sopa",mutableListOf<String>("Hervir agua","Tirar el sobrecito"),true))
        list.add(Receta("10","Canelones",mutableListOf<String>("Enrollar la comida","Cocinarla"),true))
        list.add(Receta("11","Papas fritas",mutableListOf<String>("Cortalas en juliana","Freilas"),true))
        list.add(Receta("12","Ayuno",mutableListOf<String>("No comas nada"),true))
        list.add(Receta("13","Cerdo",mutableListOf<String>("Carnealo","comelo"),true))
        list.add(Receta("14","Ensalada",mutableListOf<String>("Mezcla verduras","Poneles aceite"),true))
        list.add(Receta("15","Pascualina",mutableListOf<String>("Llena una masa con acelga","ponela al horno"),true))
        list.add(Receta("16","Empanadas",mutableListOf<String>("Rellena tapas de empanada","cocinalas"),true))
        return list;
    }

    val BASE_URL = "http://10.0.1.232:9099/asd"

    fun sendPost(body:JsonObject) {
        Thread(Runnable{
            val connection = URL(BASE_URL).openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type","application/json")
            connection.setRequestProperty("Accept","application/json")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.doInput = true
            connection.doOutput = true

            val output = DataOutputStream(connection.getOutputStream())
            output.writeBytes(body.toString())
            output.close()

            val response= StringBuilder()
            try{
                val reader = InputStreamReader(connection.inputStream)
                reader.use { input ->
                    val bufferedReader = BufferedReader(input)
                    bufferedReader.forEachLine { response.append(it.trim()) }
                }
            } catch (e : Exception){
                e.printStackTrace()
            }
            connection.disconnect()
            userInputViewModel.appStatus.value.response = response.toString()
        }).start()

    }

}