package fi.mckits.credits.util

import org.bukkit.Bukkit
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CompletableFuture

val auditLog by lazy {
    val dataFolder = Bukkit.getPluginManager().getPlugin("McKitsCredits").dataFolder
    File(dataFolder, "audit.txt").apply { createNewFile() }
}

fun auditLog(text: String) {
    CompletableFuture.runAsync {
        synchronized(auditLog) {
            FileOutputStream(auditLog, true).bufferedWriter().use { out ->
                out.newLine()
                out.write("${SimpleDateFormat().format(Date())}: $text")
            }
        }
    }
}