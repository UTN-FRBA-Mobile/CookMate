package com.utn.cookmate

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class NotifApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val notificationChannel= NotificationChannel(
            "NotificacionTimer",
            "TimedOut",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}