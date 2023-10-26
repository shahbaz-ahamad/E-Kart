package com.example.e_kart.helper


fun Float?.getProductPrice(price: Float, offerPercentage: Float): Float {
    if (this == null || this <= 0f || offerPercentage <= 0f) {
        // If offer percentage is invalid or null, return the original price
        return price
    }

    // Calculate the discounted price after applying the offer percentage
    val discount = price * (offerPercentage / 100)
    val discountedPrice = price - discount

    return discountedPrice
}
