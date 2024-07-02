package com.utn.cookmate.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.utn.cookmate.R

class CronometroService : Service() {

    private var timer: CountDownTimer? = null
    private val channelId = "timer_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val duration = intent?.getIntExtra("duration", 0) ?: 0

        timer?.cancel()
        timer = object : CountDownTimer((duration * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val notification = NotificationCompat.Builder(this@CronometroService, channelId)
                    .setContentTitle("Paso de la receta")
                    .setContentText("Tiempo restante: ${secondsRemaining / 60}:${secondsRemaining % 60}")
                    .setSmallIcon(R.drawable.ic_timer)
                    .setSilent(true)
                    .build()
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, notification)
            }

            override fun onFinish() {
                stopSelf()
            }
        }.start()

        startForeground(1, createInitialNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)

        return START_NOT_STICKY
    }

    private fun createInitialNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Cronómetro")
            .setContentText("Tiempo restante")
            .setSmallIcon(R.drawable.ic_timer)
            .setSilent(true) // Agrega esto para evitar ruidos
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer Channel"
            val descriptionText = "Channel for Timer"
            val importance = NotificationManager.IMPORTANCE_LOW // Cambia a IMPORTANCE_LOW para evitar ruidos
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
