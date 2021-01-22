package fi.mckits.credits.util

import org.bukkit.ChatColor


fun String.translateColors(): String = ChatColor.translateAlternateColorCodes('&', this)