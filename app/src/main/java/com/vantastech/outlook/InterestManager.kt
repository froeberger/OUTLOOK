package com.vantastech.outlook

import android.content.Context
import android.util.Log
import java.io.File
import java.nio.charset.Charset

class InterestManager(private val context: Context) {
    private val interestsFilePath = context.filesDir.absolutePath + "/interests.txt"
    private var lastModified: Long = 0
    private val interests = mutableListOf<Interest>()

    data class Interest(val name: String, val address: String)

    fun loadInterests() {
        val configFile = File(interestsFilePath)
        if (configFile.exists()) {
            val lastModifiedTime = configFile.lastModified()
            if (lastModifiedTime > lastModified) {
                lastModified = lastModifiedTime
                interests.clear()
                val sanitizedLines = configFile.readLines(Charset.defaultCharset())
                    .mapNotNull { sanitizeInterest(it) }
                configFile.writeText(sanitizedLines.joinToString("\n"), Charset.defaultCharset())
                sanitizedLines.forEach { line ->
                    val (name, address) = line.split("@")
                    interests.add(Interest(name, address))
                }
            }
        }
    }

    fun hasInterests(): Boolean = interests.isNotEmpty()

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
                Log.e("InterestManager", "Invalid address length for interest: $line")
                null
            }
        }
    }

    fun getInterests(): List<Interest> = interests
}
