package fi.mckits.credits.data

import fi.mckits.credits.McKitsCredits
import org.bukkit.Bukkit
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

const val CREDITS_TABLE = "mccredits"

class SQLPlayerRepository : PlayerRepository {

    private lateinit var connection: Connection

    override fun onEnable(plugin: McKitsCredits) {
        val host = plugin.config.getString("database.host")
        val port = plugin.config.getInt("database.port")
        val db = plugin.config.getString("database.db-name")
        val user = plugin.config.getString("database.user")
        val password = plugin.config.getString("database.password")
        connection = DriverManager.getConnection(
            "jdbc:mysql://$host:$port/$db?autoReconnect=true&useUnicode=yes", user, password
        )
        try {
            connection.prepareStatement(
                """
                CREATE TABLE IF NOT EXISTS $CREDITS_TABLE
                (
                    uuid VARCHAR(36) NOT NULL,
                    username VARCHAR(16) NOT NULL,
                    credits INT DEFAULT 0,
                    creditsEarned INT DEFAULT 0,
                    CONSTRAINT credits_pk PRIMARY KEY (uuid)
                );
                """
            ).execute()
            Bukkit.getLogger().info("Credits table created")
        } catch (e: Exception) {
            Bukkit.getLogger().warning("Failed to create table (already exists?)")
            e.printStackTrace()
        }
        Bukkit.getLogger().info("Database connected and initialized")
    }

    override fun createPlayer(uuid: UUID, data: PlayerData) {
        try {
            val prep = connection.prepareStatement(
                """
                    INSERT INTO $CREDITS_TABLE
                    (uuid, username, credits, creditsEarned) VALUES (?, ?, ?, ?)
                """
            )
            prep.setString(1, uuid.toString())
            prep.setString(2, data.username)
            prep.setString(3, data.credits.toString())
            prep.setString(4, data.creditsEarned.toString())
            prep.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun loadPlayer(uuid: UUID): PlayerData? {
        try {
            val statement = connection.prepareStatement("SELECT * FROM $CREDITS_TABLE WHERE uuid=?")
            statement.setString(1, uuid.toString())
            val result = statement.executeQuery()
            if (result.next()) {
                return PlayerData(
                    uuid = UUID.fromString(result.getString("uuid")),
                    username = result.getString("username"),
                    credits = result.getInt("credits"),
                    creditsEarned = result.getInt("creditsEarned"),
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun savePlayer(data: PlayerData) {
        try {
            val statement = connection.prepareStatement(
                """
                UPDATE $CREDITS_TABLE
                  SET username=?, credits=?, creditsEarned=?
                  WHERE uuid=?
                """
            )
            statement.setString(1, data.username)
            statement.setInt(2, data.credits)
            statement.setInt(3, data.creditsEarned)
            statement.setString(4, data.uuid.toString())
            statement.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}