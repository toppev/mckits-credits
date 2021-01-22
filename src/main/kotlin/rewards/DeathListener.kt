package fi.mckits.credits.listeners

import fi.mckits.credits.DEAD_PENALTY
import fi.mckits.credits.KILL_REWARD
import fi.mckits.credits.McKitsCredits
import ga.strikepractice.StrikePractice
import ga.strikepractice.fights.other.FFAFight
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

const val FFA_ONLY = true

class DeathListener(
    private val plugin: McKitsCredits
) : Listener {

    private val rewardRange = (10..25)

    @EventHandler(priority = EventPriority.NORMAL)
    fun onDeath(e: PlayerDeathEvent) {
        if (FFA_ONLY && StrikePractice.getAPI().getFight(e.entity) !is FFAFight) {
            return
        }

        val killer: Player? = e.entity.killer
        val reward = rewardRange.random()
        if (killer != null && killer !== e.entity) {
            val data = plugin.playerDataManager.getOnlineData(killer)
            if (data != null) {
                data.addCredits(reward)
                killer.sendMessage(KILL_REWARD.invoke(reward))
            } else {
                Bukkit.getLogger().warning("${killer.name} could not be rewarded because their data hasn't loaded???")
            }
        }
        val deadData = plugin.playerDataManager.getOnlineData(e.entity)
        if (deadData != null) {
            var penalty = reward / 2
            if (deadData.credits - penalty < 0) penalty = deadData.credits
            if (penalty > 0) {
                deadData.removeCredits(penalty)
                e.entity.sendMessage(DEAD_PENALTY.invoke(penalty))
            }
        } else {
            Bukkit.getLogger()
                .warning("${e.entity.name} could not take credits because their data hasn't loaded???")
        }
    }
}