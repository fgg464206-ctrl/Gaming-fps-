package com.fgg464206.gamingfps.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.fgg464206.gamingfps.R
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class PerformanceMonitorService : Service() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    companion object {
        private const val CHANNEL_ID = "performance_monitor_channel"
        private const val NOTIFICATION_ID = 1002
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        startPerformanceMonitoring()
        return START_STICKY
    }

    private fun startPerformanceMonitoring() {
        scope.launch {
            while (isActive) {
                try {
                    val cpuUsage = getCpuUsage()
                    val memoryUsage = getMemoryUsage()
                    val temperature = getDeviceTemperature()
                    val battery = getBatteryLevel()

                    val intent = Intent("com.fgg464206.gamingfps.PERFORMANCE_UPDATE").apply {
                        putExtra("cpu", cpuUsage)
                        putExtra("memory", memoryUsage)
                        putExtra("temperature", temperature)
                        putExtra("battery", battery)
                    }
                    sendBroadcast(intent)
                    delay(1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getCpuUsage(): Float {
        return try {
            val cpuFile = File("/proc/stat")
            val reader = BufferedReader(InputStreamReader(cpuFile.inputStream()))
            val line = reader.readLine()
            reader.close()

            val tokens = line?.split("\\s+".toRegex()) ?: return 0f
            if (tokens.size < 5) return 0f

            val idle = tokens[4].toLongOrNull() ?: 0L
            val total = tokens.drop(1).sumOf { it.toLongOrNull() ?: 0L }

            if (total == 0L) 0f else ((total - idle) * 100f / total)
        } catch (e: Exception) {
            0f
        }
    }

    private fun getMemoryUsage(): Int {
        val runtime = Runtime.getRuntime()
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = totalMemory - freeMemory

        return ((usedMemory * 100) / totalMemory).toInt()
    }

    private fun getDeviceTemperature(): Float {
        return try {
            val thermalFile = File("/sys/class/thermal/thermal_zone0/temp")
            if (thermalFile.exists()) {
                val temp = thermalFile.readText().trim().toFloatOrNull() ?: 0f
                temp / 1000f
            } else {
                0f
            }
        } catch (e: Exception) {
            0f
        }
    }

    private fun getBatteryLevel(): Int {
        return try {
            val batFile = File("/sys/class/power_supply/battery/capacity")
            if (batFile.exists()) {
                batFile.readText().trim().toIntOrNull() ?: 0
            } else {
                0
            }
        } catch (e: Exception) {
            0
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Performance Monitor Active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Performance Monitor",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
