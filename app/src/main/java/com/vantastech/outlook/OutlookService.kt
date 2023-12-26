package com.vantastech.outlook

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class OutlookService : Service() {

    private val scope = CoroutineScope(Dispatchers.Default)
    private lateinit var interestManager: InterestManager
    private lateinit var bluetoothScanner: BluetoothScanner
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        interestManager = InterestManager(applicationContext)
        bluetoothScanner = BluetoothScanner(applicationContext)
        notificationManager = NotificationManager(applicationContext)
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            interestManager.loadInterests()
            if (interestManager.hasInterests()) {
                bluetoothScanner.startScanning()
            } else {
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothScanner.stopScanning()
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, NotificationManager.CHANNEL_ID)
            .setContentTitle("Outlook Service")
            .setContentText("Scanning for Bluetooth devices of interest.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(NotificationManager.NOTIFICATION_ID, notification)
    }
}
