package com.vantastech.outlook

import android.util.Log
import java.io.File
import java.nio.charset.Charset

class Sanitizer(private val context: Context) {

    private val interestsFilePath = context.filesDir.absolutePath + "/interests.txt"

    fun sanitizeInterestsFile() {
        val configFile = File(interestsFilePath)
        if (configFile.exists()) {
            val sanitizedLines = configFile.readLines(Charset.defaultCharset())
                .mapNotNull { sanitizeInterest(it) }
            configFile.writeText(sanitizedLines.joinToString("\n"), Charset.defaultCharset())
        }
    }

    private fun sanitizeInterest(line: String): String? {
        val parts = line.split("@")
        if (parts.size != 2) return null

        val name = parts[0].filter { it.isLetterOrDigit() }.uppercase()
        val address = parts[1].filterNot { it.isWhitespace() }.uppercase()
            .filter { it in '0'..'9' || it in 'A'..'F' }

        return when (address.length) {
            6, 12 -> {
                val sanitizedAddress = address.chunked(2).joinToString(":")
                "$name@$sanitizedAddress"
            }
            else -> {
                Log.e("Sanitizer", "Invalid address length for interest: $line")
                null
            }
        }
    }
}
