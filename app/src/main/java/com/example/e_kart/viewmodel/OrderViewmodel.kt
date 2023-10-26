package com.example.e_kart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.datamodel.order.Order
import com.example.e_kart.util.Resource1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _order = MutableStateFlow<Resource1<Order>>(Resource1.Unspecified())
    val order = _order.asStateFlow()

    private val _allOrder = MutableStateFlow<Resource1<List<Order>>>(Resource1.Unspecified())
    val allorder = _allOrder.asStateFlow()


    init {

        getAllOrder()
    }
    fun placeOrder(order: Order) {
        viewModelScope.launch {
            _order.emit(Resource1.Loading())
        }

        firestore.runBatch { batch ->


            //add the order into the user order collection
            firestore.collection("Users").document(firebaseAuth.currentUser?.uid.toString())
                .collection("Order")
                .document()
                .set(order)

            //add the order into the order collection
            firestore.collection("Order").document().set(order)

            //delete the product form the user_cart collection
            firestore.collection("Users").document(firebaseAuth.currentUser?.uid.toString())
                .collection("cart").get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        it.reference.delete()
                    }
                }

        }.addOnSuccessListener {
            viewModelScope.launch {
                _order.emit(Resource1.Success(order))
            }
        }
            .addOnFailureListener {
                viewModelScope.launch {
                    _order.emit(Resource1.Error(it.message.toString()))
                }
            }

    }

    fun getAllOrder(){
        viewModelScope.launch {
            _allOrder.emit(Resource1.Loading())
        }

        firestore.collection("Users").document(firebaseAuth.currentUser?.uid.toString()).collection("Order")
            .get()
            .addOnSuccessListener {

                val order = it.toObjects(Order::class.java)
                viewModelScope.launch {

                    _allOrder.emit(Resource1.Success(order))
                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _allOrder.emit(Resource1.Error(it.message.toString()))
                }
            }
    }
}