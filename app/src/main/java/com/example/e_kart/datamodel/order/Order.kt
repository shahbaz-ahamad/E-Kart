package com.example.e_kart.datamodel.order

import android.os.Parcelable
import com.example.e_kart.datamodel.Address
import com.example.e_kart.datamodel.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong

@Parcelize
data class Order(
    val orderStatus : String,
    val totalPrice: Float,
    val product : List<CartProduct>,
    val address: Address,
    val date : String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val orderId : Long = nextLong(0,1000000000000)+ totalPrice.toLong()
):Parcelable{
    // No-argument constructor
    constructor() : this(
        orderStatus = "",
        totalPrice = 0.0f,
        product = emptyList(),
        address = Address(), // Assuming Address has a no-argument constructor
        date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
        orderId = 0L
    )
}
