package fi.mckits.credits

import fi.mckits.credits.util.auditLog
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

class CreditsCommand(
    private val plugin: McKitsCredits
) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String?,
        args: Array<out String>
    ): Boolean {

        val name = if (args.isEmpty()) sender.name else args[0]
        if (args.isNotEmpty() && arrayOf("?", "help", "apua").contains(name.toLowerCase())) {
            sendHelp(sender)
            return true;
        }
        CompletableFuture.runAsync {
            val of = Bukkit.getOfflinePlayer(name)
            if (of == null || !of.hasPlayedBefore()) {
                sender.sendMessage("§c$name ei ole pelannut täällä.")
            } else {
                val uuid = of.uniqueId
                val subCommand = if (args.size >= 2) args[1].toLowerCase() else null
                val isAdminCommand = listOf("give", "take", "set").contains(subCommand)
                val data = plugin.playerDataManager.findUser(uuid, isAdminCommand)
                val oldCredits = data?.credits ?: 0
                if (args.size == 3 && sender.hasPermission("mckitscredits.admin")) {
                    if (isAdminCommand && (args.size < 3 || args[2].toIntOrNull() ?: -1 < 0)) {
                        sendHelp(sender)
                        return@runAsync
                    }
                    val amount = args[2].toInt()
                    if (subCommand == "give" && data != null) {
                        data.addCredits(amount)
                    } else if (subCommand == "take" && data != null) {
                        data.removeCredits(amount)
                    } else if (subCommand == "set" && data != null) {
                        data.credits = amount
                    } else {
                        sendHelp(sender)
                        if (data == null) {
                            sender.sendMessage("§cData not found???")
                        }
                    }
                    if (isAdminCommand && data != null) {
                        plugin.playerDataManager.save(data)
                        auditLog(
                            "${sender.name} updated (/${args.joinToString(separator = " ")}) " +
                                    "${of.name}/${of.uniqueId} credits to ${data.credits}"
                        )
                        val msg = "§eUpdated ${data.username} credits from $oldCredits to ${data.credits} "
                        Bukkit.getLogger().info(msg)
                        sender.sendMessage(msg)
                        sender.sendMessage("§aCredits saved")
                        val target = Bukkit.getPlayer(uuid)
                        if (target != null) {
                            if (oldCredits < data.credits) {
                                target.sendMessage("§eSinulle annettiin §a$amount §ekrediittiä! Sinulla on nyt §a${data.credits} §ekrediittiä.")
                                target.playSound(target.location, Sound.LEVEL_UP, 1f, 1f)
                            } else if (oldCredits > data.credits) {
                                target.sendMessage("§cMenetit §a$amount §ckrediittiä! Sinulla on nyt §a${data.credits} §ckrediittiä.")
                            }
                        }
                    }
                } else {
                    if (data == null) {
                        sender.sendMessage("§cPelaajan $name krediittejä ei löytynyt.")
                    } else {
                        sender.sendMessage("§ePelaajalla $name on §a${data.credits} §ekrediittiä.")
                    }
                }
            }
        }
        return true
    }

    private fun sendHelp(sender: CommandSender) {
        sender.sendMessage("§bMcKits - Credits")
        sender.sendMessage("§e/credits [pelaaja]")
        if (sender.hasPermission("mckitscredits.admin")) {
            sender.sendMessage("§e/credits <player> give <amount>")
            sender.sendMessage("§e/credits <player> set <amount>")
            sender.sendMessage("§e/credits <player> take <amount>")
            sender.sendMessage("§7Amount must be positive.")
        }
    }
}