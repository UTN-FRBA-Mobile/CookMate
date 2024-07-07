package com.utn.cookmate.connection

import android.net.ConnectivityManager
//import android.net.NetworkCapabilities
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.withContext
//import java.net.Socket
//
class NetworkChecker (val connectivityManager: ConnectivityManager){
//
//    fun performAction(action : () -> Unit){
//         if(hasValidInternetConnection()){
//             action()
//         }
//    }
//
//    private fun hasValidInternetConnection() : Boolean{
//        val network = connectivityManager?.activeNetwork
//        val capabilities= connectivityManager?.getNetworkCapabilities(network) ?:return false
//        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
//    }
//
//    var isConnected = false
//    var i=0
//    @Composable
//    fun checker(){
//        LaunchedEffect(Unit) {
//            while(true) {
//                isConnected()
//                println("AAAAAAAA " + i)
//                i++
//                delay(5000)
//            }
//        }
//    }
//
//    @Composable
//    fun isConnected(): Boolean {
//        val host = "www.google.com"
//        val port = 80
//        try{
//            val socket = Socket(host, port)
//            isConnected = socket.isConnected
//            socket.close()
//        } catch (e: Exception) {
//            // e.printStackTrace()
//        }
//        return isConnected
//    }
//
}