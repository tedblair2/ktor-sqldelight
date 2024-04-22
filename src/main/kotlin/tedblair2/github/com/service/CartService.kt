package tedblair2.github.com.service

import tedblair2.github.com.model.Cart
import tedblair2.github.com.model.CartItem

interface CartService {
    suspend fun getCartItems():List<CartItem>
    suspend fun insertCartItems(cartItem: CartItem)
    suspend fun getCartItem(userId:Int):CartItem?
    suspend fun addNewItemToCart(userId: Int,cart: Cart)
    suspend fun updateItemQuantityInCart(userId: Int,cart: Cart)
    suspend fun deleteItemFromCart(userId: Int,cart: Cart)
}