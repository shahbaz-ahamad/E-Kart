package com.example.e_kart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.Firebase.FirebaseDB
import com.example.e_kart.datamodel.CartProduct
import com.example.e_kart.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailsViewmodel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseDB: FirebaseDB
) : ViewModel(){



    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Loading)
    val addToCart = _addToCart.asStateFlow()



    fun addUpdateProductInCart( cartProduct: CartProduct) {

        firestore.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) { //Add new product
                        addNewProduct(cartProduct)
                    } else {
                        val product = it.first().toObject(CartProduct::class.java)
                        if(product?.product == cartProduct.product && product.selectedColor == cartProduct.selectedColor && product.selectedSize== cartProduct.selectedSize){ //Increase the quantity (fixed quantity increasement issue)
                            val documentId = it.first().id
                            increaseQuantity(documentId, cartProduct)
                        } else { //Add new product
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.value=Resource.Error(it)
                }
            }
    }


    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseDB.addProductToCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.value=Resource.Success(addedProduct!!)
                else
                  _addToCart.value=Resource.Error(e)
            }
        }
    }


    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        firebaseDB.increaseQuantity(documentId) { _, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.value=Resource.Success(cartProduct)
                else
                    _addToCart.value=Resource.Error(e)
            }
        }
    }
}