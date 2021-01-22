package fi.mckits.credits.data

import fi.mckits.credits.McKitsCredits
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PlayerDataManager(
    private val plugin: McKitsCredits
) {

    private val playerRepository = (
            if (plugin.config.getBoolean("database.enabled")) SQLPlayerRepository()
            else TestMemoryPlayerRepository())
        .also {
            Bukkit.getLogger().info("Using ${it.javaClass.name} PlayerRepository implementation")
            it.onEnable(plugin)
        }

    companion object {
        private val cache = ConcurrentHashMap<UUID, PlayerData>()
    }

    /**
     * Find from cache or repository.
     * Also caches if created or fetched from the DB.
     * Possibly blocking.
     */
    fun findUser(uuid: UUID, create: Boolean): PlayerData? {
        var data = cache[uuid]
        if (data == null) {
            data = playerRepository.loadPlayer(uuid)
            if (data == null && create) {
                data = PlayerData(uuid, Bukkit.getOfflinePlayer(uuid).name)
                playerRepository.createPlayer(uuid, data)
                Bukkit.getLogger().info("Created database entry for $uuid credits")
            }
            if (data != null) {
                cache[uuid] = data
            }
        }
        return data
    }

    fun getOnlineData(player: Player) = cache[player.uniqueId]

    fun saveAll() {
        val st = System.currentTimeMillis()
        Bukkit.getOnlinePlayers().forEach {
            save(it.uniqueId)
        }
        val size = Bukkit.getOnlinePlayers().size
        if (size > 0) {
            Bukkit.getLogger().info("Saved all credits of $size players in ${System.currentTimeMillis() - st} ms.")
        }
    }

    fun removeFromCache(uuid: UUID) = cache.remove(uuid)

    fun save(uuid: UUID) {
        val data = cache[uuid]
        if (data != null) {
            save(data)
        }
    }

    fun save(data: PlayerData) {
        cache[data.uuid] = data
        playerRepository.savePlayer(data)
    }
}