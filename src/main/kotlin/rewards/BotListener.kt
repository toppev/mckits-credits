package fi.mckits.credits.rewards

import fi.mckits.credits.BOT_KILL_REWARD
import fi.mckits.credits.McKitsCredits
import ga.strikepractice.events.BotDuelEndEvent
import ga.strikepractice.npc.CitizensNPC
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class BotListener(
    private val plugin: McKitsCredits
) : Listener {


    @EventHandler
    fun onBotDuelEnd(e: BotDuelEndEvent) {
        if (e.winner == e.player.name) {
            val reward = when (e.fight.difficulty) {
                CitizensNPC.Difficulty.EASY -> 2
                CitizensNPC.Difficulty.NORMAL -> 5
                CitizensNPC.Difficulty.HARD -> 10
                CitizensNPC.Difficulty.HACKER -> 25
                else -> 10
            }
            val data = plugin.playerDataManager.getOnlineData(e.player)
            if (data != null) {
                data.addCredits(reward)
                val difficulty = e.fight.difficulty.displayName
                e.player.sendMessage(BOT_KILL_REWARD.invoke(reward, difficulty))
            }
        }
    }
}