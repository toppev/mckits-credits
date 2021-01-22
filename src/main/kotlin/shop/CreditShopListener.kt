package fi.mckits.credits.shop

import fi.mckits.credits.ALREADY_HAVE_THAT
import fi.mckits.credits.McKitsCredits
import fi.mckits.credits.SHOP_INVENTORY_TITLE
import fi.mckits.credits.YOU_BOUGHT
import fi.mckits.credits.util.auditLog
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.concurrent.CompletableFuture

class CreditShopListener(
    private val plugin: McKitsCredits
) : Listener {


    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.view.title == SHOP_INVENTORY_TITLE) {
            e.isCancelled = true
            if (e.whoClicked is Player && e.currentItem != null) {
                val p = e.whoClicked as Player
                val item = plugin.shopManager.items.firstOrNull { it.slot == e.slot }
                if (item != null) {
                    p.closeInventory()
                    val displayName = item.icon.itemMeta.displayName
                    if (item.price == -1) {
                        if (displayName.isNotBlank()) p.sendMessage(displayName);
                        item.icon.itemMeta.lore?.forEach { p.sendMessage(it) }
                    } else if (p.hasPermission(item.permission)) {
                        p.sendMessage(ALREADY_HAVE_THAT)
                    } else {
                        // purchase
                        val data = plugin.playerDataManager.getOnlineData(p)
                        if (data != null) {
                            if (data.removeCredits(item.price)) {
                                auditLog(
                                    "${p.name} purchased $displayName (${item.permission}) with ${item.price} credits." +
                                            "They now have ${data.credits} credits"
                                )
                                CompletableFuture.runAsync { plugin.playerDataManager.save(data) }
                                item.commands.forEach { cmd ->
                                    Bukkit.dispatchCommand(
                                        Bukkit.getConsoleSender(),
                                        cmd
                                            .replace("<player>", p.name)
                                            .replace("<price>", item.price.toString())
                                            .replace("<name>", displayName)
                                    )
                                }
                                p.sendMessage(YOU_BOUGHT.invoke(displayName, item.price, data.credits))
                            }
                        }
                    }
                }
            }
        }
    }
}