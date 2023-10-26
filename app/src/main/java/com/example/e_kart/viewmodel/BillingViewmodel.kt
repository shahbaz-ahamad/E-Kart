package com.example.e_kart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.datamodel.Address
import com.example.e_kart.util.Resource1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BillingViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
)  :ViewModel() {

    private  val _address = MutableStateFlow<Resource1<List<Address>>>(Resource1.Unspecified())
    val address = _address.asStateFlow()

    init {

        getUserAddress()
    }

    fun getUserAddress(){
        viewModelScope.launch {
            _address.emit(Resource1.Loading())
        }
        firestore.collection("Users").document(auth.currentUser?.uid.toString())
            .collection("Address")
            .addSnapshotListener{ value, error ->

                if(error != null){
                    viewModelScope.launch {
                        _address.emit(Resource1.Error(error.message.toString()))

                    }
                    return@addSnapshotListener
                }
                val address = value?.toObjects(Address::class.java)
                viewModelScope.launch {
                    _address.emit(Resource1.Success(address!!))

                }
            }
    }
}