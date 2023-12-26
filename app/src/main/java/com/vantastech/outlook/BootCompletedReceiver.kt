package com.vantastech.outlook

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class BootCompletedReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val serviceIntent = Intent(context, OutlookService::class.java)
            // Starting the service in the foreground to comply with Android 8+ background execution limits
            context.startForegroundService(serviceIntent)
        }
    }
}
