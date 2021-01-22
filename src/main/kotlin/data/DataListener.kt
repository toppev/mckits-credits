package fi.mckits.credits.data

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.concurrent.CompletableFuture


class DataListener(
    private val playerDataManager: PlayerDataManager
) : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val uuid = e.player.uniqueId
        CompletableFuture.runAsync {
            val st = System.currentTimeMillis()
            playerDataManager.findUser(uuid, true)
            Bukkit.getLogger().info("Loaded ${e.player.name} credit data in ${System.currentTimeMillis() - st} ms")
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        val uuid = e.player.uniqueId
        CompletableFuture.runAsync {
            val st = System.currentTimeMillis()
            playerDataManager.save(uuid)
            playerDataManager.removeFromCache(uuid)
            Bukkit.getLogger().info("Saved ${e.player.name} credit data in ${System.currentTimeMillis() - st} ms")
        }
    }
}