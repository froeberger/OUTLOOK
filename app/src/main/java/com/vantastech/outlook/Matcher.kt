package com.vantastech.outlook

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Matcher(private val context: Context, private val interestManager: InterestManager) {

    suspend fun matchDevice(scanResult: ScanResult) = withContext(Dispatchers.Default) {
        val deviceAddress = scanResult.device.address
        val interests = interestManager.getInterests()

        for (interest in interests) {
            if (matchesInterest(deviceAddress, interest.address)) {
                notifyMatchFound(interest, deviceAddress)
                break
            }
        }
    }

    private fun matchesInterest(deviceAddress: String, interestAddress: String): Boolean {
        return when {
            interestAddress.length == 17 -> deviceAddress.equals(interestAddress, ignoreCase = true)
            interestAddress.length == 8 -> deviceAddress.startsWith(interestAddress, ignoreCase = true)
            else -> false
        }
    }

    private fun notifyMatchFound(interest: InterestManager.Interest, deviceAddress: String) {
        val notificationManager = NotificationManager(context)
        val alarmManager = AlarmManager(context)

        notificationManager.showNotification(
            title = "Match Found!",
            message = "Device: $deviceAddress matched with Interest: ${interest.name} - ${interest.address}"
        )
        alarmManager.playAlarmSound()
    }
}
