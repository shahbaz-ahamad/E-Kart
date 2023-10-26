package com.example.e_kart.datamodel.order

sealed class OrderStatus(val status: String) {

    object Ordered : OrderStatus("Ordered")
    object Confirmed : OrderStatus("Confirmed")
    object Shipped : OrderStatus("Shipped")
    object Cancel : OrderStatus("Cancel")
    object Delivered : OrderStatus("Delivered")
    object Returned : OrderStatus("Returned")

}

fun getOrderStatus(status: String): OrderStatus {
    return when (status) {
        "Ordered" -> {
            OrderStatus.Ordered
        }

        "Canceled" -> {
            OrderStatus.Cancel
        }

        "Confirmed" -> {
            OrderStatus.Confirmed
        }

        "Shipped" -> {
            OrderStatus.Shipped
        }

        "Delivered" -> {
            OrderStatus.Delivered
        }

        else -> OrderStatus.Returned
    }
}

