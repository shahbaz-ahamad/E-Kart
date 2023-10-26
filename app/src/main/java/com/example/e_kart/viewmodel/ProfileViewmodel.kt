package com.example.e_kart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.datamodel.User
import com.example.e_kart.util.Resource1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel() {

    private val _user = MutableStateFlow<Resource1<User>>(Resource1.Unspecified())
    val user = _user.asStateFlow()

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            _user.emit(Resource1.Loading())
        }
        firestore.collection("Users").document(auth.currentUser?.uid.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    viewModelScope.launch {
                        _user.emit(Resource1.Error(error.message.toString()))
                    }
                } else {
                    val user = value?.toObject(User::class.java)
                    user?.let {
                        viewModelScope.launch {
                            _user.emit(Resource1.Success(user))
                        }
                    }
                }
            }
    }

    fun logout(){
        auth.signOut()
    }

}
