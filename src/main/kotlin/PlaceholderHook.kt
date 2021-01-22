package fi.mckits.credits

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PlaceholderHook(
    private val plugin: McKitsCredits
) : PlaceholderExpansion() {

    override fun getIdentifier() = "McKitsCredits"

    override fun getAuthor() = plugin.description.authors.toString()

    override fun getVersion(): String = plugin.description.version

    override fun onPlaceholderRequest(p: Player?, str: String?): String? {
        if (str == null || p == null) return null
        return when (str) {
            "credits" -> plugin.playerDataManager.getOnlineData(p)?.credits ?: 0
            else -> null
        }.toString()
    }

}