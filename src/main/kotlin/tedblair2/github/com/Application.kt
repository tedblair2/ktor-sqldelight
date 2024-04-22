package tedblair2.github.com

import io.ktor.server.application.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import tedblair2.github.com.plugins.*
import tedblair2.github.com.service.CartServiceImpl
import tedblair2.github.com.service.UserServiceImpl

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val db=configureDatabase()
    val userService=UserServiceImpl(db)
    val cartService=CartServiceImpl(db)
    configureSerialization()
    configureRouting(userService,cartService)
}
