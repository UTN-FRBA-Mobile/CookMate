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
import androidx.core.app.NotificationCompat
import com.utn.cookmate.R

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
                startTimer(duration.toLong())
            }
            "PAUSE" -> {
                pauseTimer()
            }
            "RESUME" -> {
                resumeTimer()
            }
            "STOP" -> {
                stopTimer()
            }
        }
        return START_NOT_STICKY
    }

    private fun startTimer(duration: Long) {
        remainingTime = duration * 1000
        timer?.cancel()
        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateNotification(remainingTime)
            }

            override fun onFinish() {
                stopSelf()
            }
        }.start()
        startForeground(1, createInitialNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
    }

    private fun pauseTimer() {
        timer?.cancel()
        isPaused = true
    }

    private fun resumeTimer() {
        isPaused = false
        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateNotification(remainingTime)
            }

            override fun onFinish() {
                stopSelf()
            }
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
        stopSelf()
    }

    private fun updateNotification(remainingTime: Long) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Paso de la receta")
            .setContentText("Tiempo restante: ${remainingTime / 60000}:${(remainingTime / 1000) % 60}")
            .setSmallIcon(R.drawable.ic_timer)
            .setSilent(true)
            .build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    private fun createInitialNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Cronómetro")
            .setContentText("Tiempo restante")
            .setSmallIcon(R.drawable.ic_timer)
            .setSilent(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer Channel"
            val descriptionText = "Channel for Timer"
            val importance = NotificationManager.IMPORTANCE_LOW
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
