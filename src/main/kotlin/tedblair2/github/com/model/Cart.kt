package tedblair2.github.com.model

import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    val itemName:String,
    val quantity:Int,
    val price:Double
)

@Serializable
data class CartItem(
    val id:Int=0,
    val userId:Int,
    val items:List<Cart>
)

@Serializable
data class CartAddition(
    val userId: Int,
    val item:Cart
)
