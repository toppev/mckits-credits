package fi.mckits.credits


const val SHOP_INVENTORY_TITLE = "§cKrediittikauppa"
const val ALREADY_HAVE_THAT = "§cOmistat jo tuon!"


val KILL_REWARD = { reward: Int -> "§eSait §c$reward §ekrediittiä taposta." }

val BOT_KILL_REWARD = { reward: Int, difficulty: String -> "§eSait §c$reward §ekrediittiä $difficulty-§ebotin taposta." }

val DEAD_PENALTY = { reward: Int -> "§eMenetit §c$reward §ekrediittiä, koska kuolit." }

val YOU_BOUGHT = { name: String, creditsUsed: Int, creditsLeft: Int ->
    "§eOstit '§c$name' §a$creditsUsed §ekrefiitillä. Sinulla on §a$creditsLeft §ekrediittiä jäljellä."
}

val YOU_WON_EVENT = { reward: Int, event: String, playersInEvent: Int ->
    "§eSait §c$reward§e, koska voitit §a$event-tapahtuman§e, jossa oli §a$playersInEvent §epelaajaa."
}