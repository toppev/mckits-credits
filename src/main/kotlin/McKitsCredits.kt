package fi.mckits.credits

import fi.mckits.credits.data.DataListener
import fi.mckits.credits.data.PlayerDataManager
import fi.mckits.credits.listeners.DeathListener
import fi.mckits.credits.rewards.BotListener
import fi.mckits.credits.rewards.HostedEventListener
import fi.mckits.credits.shop.CreditShopCommand
import fi.mckits.credits.shop.CreditShopListener
import fi.mckits.credits.shop.ShopItem
import fi.mckits.credits.shop.ShopManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.TimeUnit.MINUTES


class McKitsCredits : JavaPlugin(), CommandExecutor, TabCompleter {

    lateinit var playerDataManager: PlayerDataManager
        private set
    lateinit var shopManager: ShopManager
        private set
    private val placeholderHook = PlaceholderHook(this)

    override fun onEnable() {
        ConfigurationSerialization.registerClass(ShopItem::class.java, "ShopItem")
        playerDataManager = PlayerDataManager(this)
        saveDefaultConfig()

        shopManager = ShopManager(this).apply { load() }
        placeholderHook.register()

        val pm = Bukkit.getPluginManager()
        pm.registerEvents(DataListener(playerDataManager), this)
        pm.registerEvents(CreditShopListener(this), this)

        pm.registerEvents(HostedEventListener(this), this)
        pm.registerEvents(DeathListener(this), this)
        pm.registerEvents(BotListener(this), this)

        getCommand("mckitscredits").executor = CreditsCommand(this)
        getCommand("creditshop").executor = CreditShopCommand(this)

        Bukkit.getOnlinePlayers().forEach {
            playerDataManager.findUser(it.uniqueId, true)
        }

        val delay = 20 * MINUTES.toSeconds(20)
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, {
            playerDataManager.saveAll()
        }, delay, delay)
    }

    override fun onDisable() {
        playerDataManager.saveAll()
    }
}