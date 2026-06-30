package com.fgg464206.gamingfps.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.fgg464206.gamingfps.R

class FpsOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private var overlayView: TextView? = null
    private val handler = Handler(Looper.getMainLooper())
    private var frameCounter = 0
    private var lastSecondTime = System.currentTimeMillis()
    private var currentFps = 0

    companion object {
        private const val CHANNEL_ID = "fps_overlay_channel"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createOverlay()
        startForeground(NOTIFICATION_ID, createNotification())
        startFrameRateMonitoring()
        return START_STICKY
    }

    private fun createOverlay() {
        overlayView = TextView(this).apply {
            text = "FPS: 0"
            textSize = 16f
            setTextColor(0xFF00FF00.toInt())
            setBackgroundColor(0x99000000.toInt())
        }

        val layoutParams = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = 200
            height = 100
            gravity = Gravity.TOP or Gravity.LEFT
            x = 50
            y = 50
        }
        windowManager.addView(overlayView, layoutParams)
    }

    private fun startFrameRateMonitoring() {
        handler.post(object : Runnable {
            override fun run() {
                frameCounter++
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - lastSecondTime

                if (elapsedTime >= 1000) {
                    currentFps = frameCounter
                    overlayView?.text = "FPS: $currentFps"
                    frameCounter = 0
                    lastSecondTime = currentTime
                }
                handler.postDelayed(this, 16)
            }
        })
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("FPS Monitor Active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "FPS Overlay",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        overlayView?.let { windowManager.removeView(it) }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
