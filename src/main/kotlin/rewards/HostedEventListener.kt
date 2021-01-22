package fi.mckits.credits.rewards

import fi.mckits.credits.McKitsCredits
import fi.mckits.credits.YOU_WON_EVENT
import ga.strikepractice.events.PvPEventEndEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class HostedEventListener(
    private val plugin: McKitsCredits
) : Listener {

    @EventHandler
    fun onTournamentEnd(e: PvPEventEndEvent) {
        val p: Player? = e.winner
        if (p != null && e.event != null) {
            val reward = 50 + (e.event.totalPlayers * 10)
            val event = e.event.type.displayName ?: "tuntemattoman"
            val players = e.event.totalPlayers
            val data = plugin.playerDataManager.getOnlineData(p)
            if (data != null) {
                data.addCredits(reward)
                p.sendMessage(YOU_WON_EVENT.invoke(reward, event, players))
            } else {
                Bukkit.getLogger().warning("${p.name} couldn't get PvPEventEndEvent, no online data???")
            }
        }
    }
}