package tedblair2.github.com.service

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tedblair2.github.com.db.Database
import tedblair2.github.com.model.Cart
import tedblair2.github.com.model.CartItem

class CartServiceImpl(db:Database) : CartService {

    private val query=db.cartsQueries

    override suspend fun getCartItems(): List<CartItem> {
        return query.getCartItems(mapper=::mapCartItem).executeAsList()
    }

    override suspend fun insertCartItems(cartItem: CartItem) {
        query.transaction {
            query.insertCartItem(cartItem.userId,cartItem.items)
        }
    }

    override suspend fun getCartItem(userId: Int): CartItem? {
        return query.getUsersCartItem(userId,::mapCartItem).executeAsOneOrNull()
    }

    override suspend fun addNewItemToCart(userId: Int, cart: Cart) {
        query.transaction {
            query.addNewItemToCart(
                userid = userId,
                item = Json.encodeToString(cart)
            )
        }
    }

    override suspend fun updateItemQuantityInCart(userId: Int, cart: Cart) {
        query.transaction {
            val index=query.getUserItems(userId).executeAsOneOrNull()?.indexOfFirst {
                it.itemName==cart.itemName
            }
            index?.let { value->
                query.updateItemQuantityInCart(
                    index = value.toString(),
                    newvalue = cart.quantity.toString(),
                    userid = userId)
            }
        }
    }

    override suspend fun deleteItemFromCart(userId: Int, cart: Cart) {
        query.transaction {
            val index=query.getUserItems(userId).executeAsOneOrNull()?.indexOfFirst {
                it.itemName==cart.itemName
            }
            index?.let { value->
                query.deleteItemInCart(
                    index = value.toString(),
                    userid = userId
                )
            }
        }
    }

    private fun mapCartItem(
        id:Int,
        userId: Int,
        items:List<Cart>
    ):CartItem{
        return CartItem(id, userId, items)
    }
}