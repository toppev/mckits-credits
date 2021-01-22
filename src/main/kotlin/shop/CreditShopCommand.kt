package fi.mckits.credits.shop

import fi.mckits.credits.McKitsCredits
import fi.mckits.credits.SHOP_INVENTORY_TITLE
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreditShopCommand(
    private val plugin: McKitsCredits
) : CommandExecutor {


    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) sender.sendMessage("Only players can execute that command")
        val p = sender as Player
        if (args.isNotEmpty() && sender.hasPermission("mckitscredits.admin")) {
            if (args[0].equals("additem", false) && args.size == 5) {
                if (p.itemInHand == null || p.itemInHand.type == Material.AIR) {
                    p.sendMessage("Hold something in your hand")
                } else {
                    val item = ShopItem(
                        p.itemInHand,
                        args[1].toIntOrNull() ?: 0,
                        args[2].toIntOrNull() ?: 10,
                        args[3],
                        args[4].replace("_", " ").split(",")
                    )
                    plugin.shopManager.addItem(item)
                    p.sendMessage("§eAdded $item")
                }
            } else {
                sender.sendMessage("/creditstore")
                sender.sendMessage("/creditstore additem <slot> <price> <permission> <console commands>")
            }
        } else {
            // Open GUI
            val inv = Bukkit.createInventory(null, 54, SHOP_INVENTORY_TITLE)
            val data = plugin.playerDataManager.getOnlineData(p)
            plugin.shopManager.items.forEach {
                val icon = it.icon.clone()
                if (it.price != -1) {
                    val hasIt = p.hasPermission(it.permission)
                    val creditsNeeded = it.price - (data?.credits ?: 0)
                    val meta = icon.itemMeta
                    meta.lore = (meta.lore ?: arrayListOf()) + arrayListOf(
                        "",
                        "§eHinta: §a${it.price} §ekrediittiä",
                        when {
                            hasIt -> {
                                "§9Omistat jo tämän"
                            }
                            creditsNeeded > 0 -> {
                                "§cTarvitset $creditsNeeded krediittiä lisää"
                            }
                            else -> {
                                "§a§lOsta klikkaamalla"
                            }
                        }
                    )
                    icon.itemMeta = meta
                }
                inv.setItem(it.slot, icon)
            }
            p.openInventory(inv)
        }

        return true
    }
}