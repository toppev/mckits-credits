package fi.mckits.credits.data

import java.util.*

class PlayerData(
    val uuid: UUID,
    val username: String,
    var credits: Int = 0,
    var creditsEarned: Int = credits,
) {

    fun addCredits(amount: Int): Int {
        credits += amount
        creditsEarned += amount
        return credits
    }

    fun removeCredits(amount: Int): Boolean {
        if (credits - amount < 0) return false;
        this.credits -= amount
        return true
    }

}