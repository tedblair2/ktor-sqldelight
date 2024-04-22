package tedblair2.github.com.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import tedblair2.github.com.model.CartAddition
import tedblair2.github.com.model.CartItem
import tedblair2.github.com.model.User
import tedblair2.github.com.service.CartService
import tedblair2.github.com.service.UserService

fun Application.configureRouting(
    userService: UserService,
    cartService: CartService
) {

    routing {
        get("/"){
            val users=userService.getUsers()
            call.respond(HttpStatusCode.OK,users)
        }
        post("/") {
            val user=call.receive<User>()
            userService.insertUser(user)
            call.respond(HttpStatusCode.Created,user)
        }
        get("/{id}"){
            val id=call.parameters["id"]?.toInt()
            id?.let {
                userService.getUser(id)?.let { user->
                    call.respond(HttpStatusCode.Found,user)
                } ?: call.respond(HttpStatusCode.NotFound,"User not found!")
            } ?: call.respond(HttpStatusCode.BadRequest,"Please provide an id!!")
        }
        route("/cart"){
            get {
                val items=cartService.getCartItems()
                call.respond(HttpStatusCode.OK,items)
            }
            post {
                val item=call.receive<CartItem>()
                cartService.insertCartItems(item)
                call.respond(HttpStatusCode.Created,"Items added successfully")
            }
            get("/{userid}") {
                val id=call.parameters["userid"]?.toInt()
                id?.let {
                    cartService.getCartItem(id)?.let { cartItem ->
                        call.respond(HttpStatusCode.Found,cartItem)
                    } ?: call.respond(HttpStatusCode.NotFound,"No items present in cart")
                } ?: call.respond(HttpStatusCode.BadRequest,"Please provide an id!!")
            }
            post("/add") {
                val item=call.receive<CartAddition>()
                cartService.addNewItemToCart(item.userId,item.item)
                call.respond(HttpStatusCode.Created,"Item added successfully")
            }
            put("/update") {
                val item=call.receive<CartAddition>()
                cartService.updateItemQuantityInCart(item.userId,item.item)
                call.respond(HttpStatusCode.OK,"Item updated successfully")
            }
            delete("/remove") {
                val item=call.receive<CartAddition>()
                cartService.deleteItemFromCart(item.userId,item.item)
                call.respond(HttpStatusCode.OK,"Item deleted successfully")
            }
        }
    }
}
