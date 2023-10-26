package com.example.e_kart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.datamodel.Address
import com.example.e_kart.util.Resource1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _addnewAddress = MutableStateFlow<Resource1<Address>>(Resource1.Unspecified())
    val addnewAddress = _addnewAddress.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun addAddress(address: Address){

        val validateInput = ValidateInput(address)

        if(validateInput) {

            viewModelScope.launch {
                _addnewAddress.emit(Resource1.Loading())
            }

            firestore.collection("Users").document(auth.currentUser?.uid.toString())
                .collection("Address").document()
                .set(address)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _addnewAddress.emit(Resource1.Success(address))
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _addnewAddress.emit(Resource1.Error(it.message.toString()))
                    }
                }
        }else{
            viewModelScope.launch {
                _error.emit("All field are required")
            }

        }

    }

    private fun ValidateInput(address: Address): Boolean {

        return address.addressTitle.trim().isNotEmpty() &&
                address.fullname.trim().isNotEmpty() &&
                address.street.trim().isNotEmpty() &&
                address.phone.trim().isNotEmpty() &&
                address.city.trim().isNotEmpty() &&
                address.street.trim().isNotEmpty()
    }
}