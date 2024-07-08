package com.utn.cookmate

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class NotificationService(
    private val context:Context
){
    private val notificationManager=context.getSystemService(NotificationManager::class.java)
    fun showBasicNotification(){
        val notification=NotificationCompat.Builder(context,"NotificacionTimer")
            .setContentTitle("Aviso Cookmate!")
            .setContentText("Se ha terminado el tiempo!")
            .setSmallIcon(R.drawable.pizza)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            //.setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }

    fun showExpandableNotification(){
        val notification=NotificationCompat.Builder(context,"NotificacionTimer")
            .setContentTitle("Aviso Cookmate!")
            .setContentText("Se ha terminado el tiempo!")
            .setSmallIcon(R.drawable.pizza)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(Random.nextInt(),notification)
    }

    private fun Context.bitmapFromResource(
        @DrawableRes resId:Int
    )= BitmapFactory.decodeResource(
        resources,
        resId
    )
}