package com.fgg464206.gamingfps.shizuku

import android.content.Context
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku

class ShizukuManager(private val context: Context) {

    suspend fun isShizukuAvailable(): Boolean = withContext(Dispatchers.Default) {
        return@withContext try {
            Shizuku.pingBinder()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun hasPermission(): Boolean = withContext(Dispatchers.Default) {
        return@withContext try {
            Shizuku.checkSelfPermission(context.packageName) == PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    fun requestPermission() {
        try {
            Shizuku.requestPermission(23)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun executeCommand(command: String): String = withContext(Dispatchers.IO) {
        return@withContext try {
            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            val exitCode = process.waitFor()
            "Command executed with exit code: $exitCode"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}
