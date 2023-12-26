package com.vantastech.outlook

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.media.SoundPool
import android.os.Build

class AlarmManager(private val context: Context) {

    private val soundPool: SoundPool by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            @Suppress("DEPRECATION")
            SoundPool(1, AudioManager.STREAM_ALARM, 0)
        }
    }

    private var alarmSoundId: Int = 0

    init {
        loadAlarmSound()
    }

    private fun loadAlarmSound() {
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        alarmSoundId = soundPool.load(context, alarmUri, 1)
    }

    fun playAlarmSound() {
        if (alarmSoundId != 0) {
            soundPool.play(alarmSoundId, 1f, 1f, 1, 0, 1f)
        }
    }
}
