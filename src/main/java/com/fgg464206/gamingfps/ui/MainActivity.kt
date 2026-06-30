package com.fgg464206.gamingfps.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fgg464206.gamingfps.service.FpsOverlayService
import com.fgg464206.gamingfps.service.PerformanceMonitorService
import com.fgg464206.gamingfps.shizuku.ShizukuManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val shizukuManager by lazy { ShizukuManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.R.layout.activity_main)

        setupUI()
        checkShizukuPermission()
    }

    private fun setupUI() {
        findViewById<Switch>(android.R.id.toggle1)?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) startFpsOverlay() else stopFpsOverlay()
        }

        findViewById<Switch>(android.R.id.toggle2)?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) startPerformanceMonitor() else stopPerformanceMonitor()
        }
    }

    private fun checkShizukuPermission() {
        lifecycleScope.launch {
            val isAvailable = shizukuManager.isShizukuAvailable()
            val hasPermission = shizukuManager.hasPermission()
            
            findViewById<TextView>(android.R.id.text1)?.text = when {
                isAvailable && hasPermission -> "Shizuku: Connected ✓"
                isAvailable -> "Shizuku: Need Permission"
                else -> "Shizuku: Not Available"
            }

            if (isAvailable && !hasPermission) {
                shizukuManager.requestPermission()
            }
        }
    }

    private fun startFpsOverlay() {
        startService(Intent(this, FpsOverlayService::class.java))
        Toast.makeText(this, "FPS Overlay Started", Toast.LENGTH_SHORT).show()
    }

    private fun stopFpsOverlay() {
        stopService(Intent(this, FpsOverlayService::class.java))
        Toast.makeText(this, "FPS Overlay Stopped", Toast.LENGTH_SHORT).show()
    }

    private fun startPerformanceMonitor() {
        startService(Intent(this, PerformanceMonitorService::class.java))
        Toast.makeText(this, "Performance Monitor Started", Toast.LENGTH_SHORT).show()
    }

    private fun stopPerformanceMonitor() {
        stopService(Intent(this, PerformanceMonitorService::class.java))
        Toast.makeText(this, "Performance Monitor Stopped", Toast.LENGTH_SHORT).show()
    }
}
