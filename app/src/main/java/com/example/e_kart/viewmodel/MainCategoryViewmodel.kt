package com.example.e_kart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.datamodel.Product
import com.example.e_kart.repository.UserRepo
import com.example.e_kart.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewmodel @Inject constructor(
    private val userRepo: UserRepo
):ViewModel() {

    private  val _specialProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Loading)
    val specialProduct:StateFlow<Resource<List<Product>>> = _specialProduct

    private val _bestDeal = MutableStateFlow<Resource<List<Product>>>(Resource.Loading)
    val bestDeal:StateFlow<Resource<List<Product>>> = _bestDeal

    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Loading)
    val bestProduct:StateFlow<Resource<List<Product>>> = _bestProduct

    init {
        fetch_special_product()
        fetchBestDealItem()
        fetchBestProduct()
    }

    fun fetch_special_product(){
        viewModelScope.launch {
            _specialProduct.value=Resource.Loading
            val result=userRepo.fetchSpecialItem()
            _specialProduct.value=result
        }
    }

    fun fetchBestDealItem(){
        viewModelScope.launch {
            _bestDeal.value=Resource.Loading
            val result =userRepo.fetchBestDealItem()
            _bestDeal.value=result
        }
    }

    fun fetchBestProduct(){

        viewModelScope.launch {
            _bestProduct.value=Resource.Loading
            val result=userRepo.fetchBestProductItem()
            _bestProduct.value=result
        }
    }

}