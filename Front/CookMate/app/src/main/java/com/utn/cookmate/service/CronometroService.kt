package com.utn.cookmate.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.BitmapFactory
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.utn.cookmate.R
import kotlin.random.Random

class CronometroService : Service() {

    private var timer: CountDownTimer? = null
    private val channelId = "timer_channel"
    private var remainingTime: Long = 0
    private var isPaused: Boolean = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START" -> {
                val duration = intent.getIntExtra("duration", 0)
                val paso = intent.getStringExtra("paso")
                val imagen = intent.getByteArrayExtra("imagen")
                startTimer(duration.toLong(), paso, imagen)
            }
            "PAUSE" -> {
                pauseTimer()
            }
            "RESUME" -> {
                val paso = intent.getStringExtra("paso")
                val imagen = intent.getByteArrayExtra("imagen")
                resumeTimer(paso, imagen)
            }
            "STOP" -> {
                stopTimer()
            }
        }
        return START_NOT_STICKY
    }

    private fun startTimer(duration: Long, paso: String?, imagen: ByteArray?) {
        remainingTime = duration * 1000
        timer?.cancel()
        startForeground(1, createInitialNotification(paso, imagen), ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST)

        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateNotification(remainingTime)
            }

            override fun onFinish() {
                sendFinalNotification(paso,imagen)
                stopSelf()
            }
        }.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isPaused = true
    }

    private fun resumeTimer(paso: String?, imagen: ByteArray?) {
        isPaused = false
        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateNotification(remainingTime)
            }

            override fun onFinish() {
                sendFinalNotification(paso, imagen)
                stopSelf()
            }
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
        stopSelf()
    }

    @SuppressLint("DefaultLocale")
    private fun updateNotification(remainingTime: Long) {
        val minutes = remainingTime / 60000
        val seconds = (remainingTime / 1000) % 60

        val formattedTime = String.format("%02d:%02d", minutes, seconds)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Paso de la receta")
            .setContentText("Tiempo restante: $formattedTime")
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setSilent(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    private fun createInitialNotification(paso: String?, imagen: ByteArray?): Notification {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val bitmap = imagen?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
        val notification = NotificationCompat.Builder(this, "NotificacionTimer")
            .setContentTitle("Cookmate")
            .setContentText("Comenzaste el paso: "+ paso)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(
                        bitmap
                    )
            )
            .build()
        notificationManager.notify(Random.nextInt(), notification)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Cronómetro")
            .setContentText("Tiempo restante")
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
    }

    private fun sendFinalNotification(paso: String?, imagen: ByteArray?) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val bitmap = imagen?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

        val notification = NotificationCompat.Builder(this, "NotificacionTimer")
            .setContentTitle("Aviso Cookmate")
            .setContentText("Paso finalizado: "+ paso)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(
                        bitmap
                    )
            )
            .build()
        notificationManager.notify(Random.nextInt(), notification)
    }


    private fun createNotificationChannel() {
        val name = "Timer Channel"
        val descriptionText = "Channel for Timer"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

}
