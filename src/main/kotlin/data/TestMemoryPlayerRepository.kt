package fi.mckits.credits.data

import org.bukkit.Bukkit
import java.util.*

/**
 * "null" PlayerRepository implementation for testing
 */
class TestMemoryPlayerRepository : PlayerRepository {

    override fun createPlayer(uuid: UUID, data: PlayerData) {
        Bukkit.getLogger().warning("#createPlayer ${data.username} called. Database is disabled.")
    }

    override fun loadPlayer(uuid: UUID): PlayerData? {
        Bukkit.getLogger().warning("#loadPlayer $uuid called. Database is disabled.")
        return null
    }

    override fun savePlayer(data: PlayerData) {
        Bukkit.getLogger().warning("#savePlayer ${data.username} called. Database is disabled.")
    }
}