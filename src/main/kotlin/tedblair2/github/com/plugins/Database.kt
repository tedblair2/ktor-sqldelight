package tedblair2.github.com.plugins

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.datetime.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import migrations.Carts
import migrations.Users
import org.flywaydb.core.Flyway
import tedblair2.github.com.db.Database
import tedblair2.github.com.model.Cart

fun Application.configureDatabase():Database{
    val applicationConfig=environment.config
    val driverClass=applicationConfig.property("storage.driverClassName").getString()
    val jdbcUrl=applicationConfig.property("storage.jdbcURL").getString()
    val hikariDataSource= createHikariDataSource(jdbcUrl,driverClass)
    Flyway.configure().dataSource(hikariDataSource).load().migrate()
    val driver:SqlDriver=hikariDataSource.asJdbcDriver()
    val db=Database(
        driver = driver,
        usersAdapter = Users.Adapter(
            date_of_birthAdapter = dateColumnAdapter
        ),
        cartsAdapter = Carts.Adapter(
            itemsAdapter = jsonBColumnAdapter
        )
    )
    return db
}

private fun createHikariDataSource(url:String,driver:String): HikariDataSource {
    val hikariConfig= HikariConfig().apply {
        driverClassName=driver
        jdbcUrl=url
        maximumPoolSize=3
        isAutoCommit = true
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
    return HikariDataSource(hikariConfig)
}

val dateColumnAdapter=object:ColumnAdapter<LocalDate,java.time.LocalDate>{

    override fun decode(databaseValue: java.time.LocalDate): LocalDate {
        return LocalDate(databaseValue.year,databaseValue.month,databaseValue.dayOfMonth)
    }

    override fun encode(value: LocalDate): java.time.LocalDate {
        return value.toJavaLocalDate()
    }
}

val jsonBColumnAdapter=object :ColumnAdapter<List<Cart>,String>{

    override fun decode(databaseValue: String): List<Cart> {
        return Json.decodeFromString(databaseValue) as List<Cart>
    }

    override fun encode(value: List<Cart>): String {
        return Json.encodeToString(value)
    }
}