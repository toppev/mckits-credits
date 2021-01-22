package fi.mckits.credits.shop

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import org.bukkit.inventory.ItemStack

@SerializableAs("ShopItem")
class ShopItem(
    val icon: ItemStack,
    val slot: Int,
    val price: Int,
    val permission: String,
    val commands: Collection<String> = listOf(),
) : ConfigurationSerializable {

    @Suppress("UNCHECKED_CAST", "unused")
    constructor(serialized: Map<String, Any>) : this(
        serialized["icon"] as ItemStack,
        serialized["slot"] as Int,
        serialized["price"] as Int,
        serialized["permission"] as String,
        serialized["commands"] as Collection<String>,
    )

    override fun serialize(): MutableMap<String, Any> {
        return mutableMapOf(
            "icon" to icon,
            "slot" to slot,
            "price" to price,
            "permission" to permission,
            "commands" to commands
        )
    }

    override fun toString(): String {
        return "ShopItem(icon=$icon, slot=$slot, price=$price, permission='$permission', commands=$commands)"
    }

    @Suppress("unused")
    companion object {
        @JvmStatic
        fun deserialize(serialized: Map<String, Any>) = ShopItem(serialized)

        @JvmStatic
        fun valueOf(serialized: Map<String, Any>) = ShopItem(serialized)
    }


}