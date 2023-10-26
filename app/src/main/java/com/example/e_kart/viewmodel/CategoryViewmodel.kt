package com.example.e_kart.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.datamodel.Category
import com.example.e_kart.datamodel.Product
import com.example.e_kart.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewmodel constructor(private val firestore: FirebaseFirestore,
    private val category: Category):ViewModel() {

        private  val _offerProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Loading)
        val offerProduct = _offerProduct.asStateFlow()

        private  val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Loading)
        val bestProduct = _bestProduct.asStateFlow()

    init {
        fetchOfferProduct()
        fetchBestProduct()
    }

    fun fetchOfferProduct(){
        Log.d("category",category.category)
        firestore.collection("Product").
                    whereEqualTo("category",category.category)
            .whereNotEqualTo("offerPercentage",null).get()
            .addOnSuccessListener {
                val product = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProduct.value=Resource.Success(product)
                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _offerProduct.value=Resource.Error(it)
                }
            }
    }

    fun fetchBestProduct(){
        firestore.collection("Product").
        whereEqualTo("category",category.category)
            .whereEqualTo("offerPercentage",null).get()
            .addOnSuccessListener {
                val product = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProduct.value=Resource.Success(product)
                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _bestProduct.value=Resource.Error(it)
                }
            }
    }


}