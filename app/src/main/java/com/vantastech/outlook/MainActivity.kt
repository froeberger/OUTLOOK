package com.vantastech.outlook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * MainActivity for the OUTLOOK app.
 * Since the app is headless and has no user interface, this activity will be empty and is only used
 * to start the OutlookService when the app is manually launched.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // As there is no UI, setContentView is not called.

        // Start the OutlookService when the app is launched.
        Intent(this, OutlookService::class.java).also { intent ->
            startForegroundService(intent)
        }

        // Close the activity as it's no longer needed after starting the service.
        finish()
    }
}
