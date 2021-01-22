package fi.mckits.credits.shop

import fi.mckits.credits.McKitsCredits
import org.bukkit.Bukkit

class ShopManager(
    private val plugin: McKitsCredits
) {

    lateinit var items: Collection<ShopItem>
        private set

    fun load() {
        try {
            @Suppress("UNCHECKED_CAST")
            items = plugin.config.getList("store-items") as Collection<ShopItem>
            Bukkit.getLogger().info("Loaded ${items.size} ShopItems")
        } catch (e: Exception) {
            Bukkit.getLogger().warning("Failed to load store-items")
            e.printStackTrace()
            items = arrayListOf()
        }
    }

    fun addItem(item: ShopItem) {
        plugin.config.set("store-items", items + item)
        Bukkit.getLogger().info("Saved " + plugin.config.getList("store-items").size + " creditshop items")
        plugin.saveConfig()
        load()
    }
}