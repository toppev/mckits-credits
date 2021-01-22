package fi.mckits.credits.data

import fi.mckits.credits.McKitsCredits
import java.util.*

/**
 * All implementations should be synchronous
 */
interface PlayerRepository {

    fun onEnable(plugin: McKitsCredits) {
        /* nothing by default */
    }

    fun createPlayer(uuid: UUID, data: PlayerData)

    fun loadPlayer(uuid: UUID): PlayerData?

    fun savePlayer(data: PlayerData)
}