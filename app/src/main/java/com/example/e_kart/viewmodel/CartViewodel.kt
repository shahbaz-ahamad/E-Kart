package com.example.e_kart.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.Firebase.FirebaseDB
import com.example.e_kart.datamodel.CartProduct
import com.example.e_kart.helper.getProductPrice
import com.example.e_kart.util.Resource
import com.example.e_kart.util.Resource.Loading.getData
import com.example.e_kart.util.Resource1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.annotation.meta.When
import javax.inject.Inject

@HiltViewModel
class CartViewodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseDB: FirebaseDB
) : ViewModel() {

    private val _cartProducts = MutableStateFlow<Resource1<List<CartProduct>>>(Resource1.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deletedailog = _deleteDialog.asSharedFlow()

    private var cartProductDocument = emptyList<DocumentSnapshot>()

    //delete the cart item
    fun deleteCartItem(cartProduct: CartProduct){
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if(index !=null && index !=-1){
            val documentId= cartProductDocument[index].id
            firestore.collection("Users").document(auth.currentUser?.uid!!).collection("cart")
                .document(documentId).delete()
        }



    }

    //calculate the total price
    val productPrize =cartProducts.map {
        when(it){
            is Resource1.Success ->{

                calculatePrice(it.data!!)
            }else -> null
        }
    }

    private fun calculatePrice(data: List<CartProduct>): Float? {

        return data.sumByDouble {cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price,cartProduct.product.offerPercentage!!) * cartProduct.quantity).toDouble()
        }.toFloat()

    }



    init {
        getCartProducts()
    }

    private fun getCartProducts() {
        viewModelScope.launch { _cartProducts.emit(Resource1.Loading()) }
        firestore.collection("Users").document(auth.currentUser?.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch { _cartProducts.emit(Resource1.Error(error?.message.toString())) }
                } else {
                    cartProductDocument = value.documents
                    val cartProducts = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch { _cartProducts.emit(Resource1.Success(cartProducts)) }
                }
            }
    }



    fun changeQuantity(
        cartProduct: CartProduct,
        qunatityChanging: FirebaseDB.quantityChanging
    ){

        val index = cartProducts.value.data?.indexOf(cartProduct)

        if(index != null && index != -1){
            val documentId= cartProductDocument[index].id
            when (qunatityChanging) {
                FirebaseDB.quantityChanging.INCREASAE -> {
                    viewModelScope.launch {
                        _cartProducts.emit(Resource1.Loading())
                    }
                    increaseQuantity(documentId)
                     }

                FirebaseDB.quantityChanging.DECREASE ->{

                    if(cartProduct.quantity == 1){
                        viewModelScope.launch {
                            _deleteDialog.emit(cartProduct)
                        }

                        return
                    }
                    viewModelScope.launch {
                        _cartProducts.emit(Resource1.Loading())
                    }
                DecreaseQuantity(documentId)
            }
            }
        }
    }

    private fun DecreaseQuantity(documentId: String) {
        firebaseDB.decreaseQuantity(documentId) {result,exception ->
            if(exception != null){
                viewModelScope.launch {
                    _cartProducts.emit(Resource1.Error(exception.message.toString()))
                }
            }
        }
    }

    private fun increaseQuantity(documentId: String) {

        firebaseDB.increaseQuantity(documentId){result , exception ->
            if(exception != null){
                viewModelScope.launch {
                    _cartProducts.emit(Resource1.Error(exception.message.toString()))
                }
            }
        }
    }
}