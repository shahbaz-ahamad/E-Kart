package com.example.e_kart.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.di.BaseApp
import com.example.e_kart.datamodel.User
import com.example.e_kart.util.RegistrationValidation
import com.example.e_kart.util.Resource1
import com.example.e_kart.util.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject



@HiltViewModel
class UserAccountViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
     app: Application
) : AndroidViewModel(app) {

    private val _userState = MutableStateFlow<Resource1<User>>(Resource1.Unspecified())
    val userState = _userState.asStateFlow()

    private val _editInfo = MutableStateFlow<Resource1<User>>(Resource1.Unspecified())
    val editInfo = _editInfo.asStateFlow()


    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            _userState.emit(Resource1.Loading())
        }

        firestore.collection("Users").document(auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener {

                val user = it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch {
                        _userState.emit(Resource1.Success(user))
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _userState.emit(Resource1.Error(it.message.toString()))
                }
            }
    }

    fun updateUserInfo(user: User, imageUri: Uri?) {

        val areInputValid = validateEmail(user.email) is RegistrationValidation.Success
                && user.firstName.trim().isNotEmpty()

        if (!areInputValid) {
            viewModelScope.launch {
                _editInfo.emit(Resource1.Error("Check Input"))
            }
            return
        }

        viewModelScope.launch {
            _editInfo.emit(Resource1.Loading())
        }

        if (imageUri == null) {
                saveUserInfo(user,true)
        }
        else{
            saveUserWithImage(user,imageUri)
        }
    }

    private fun saveUserWithImage(user: User, imageUri: Uri) {

        viewModelScope.launch {

            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(
                   getApplication<BaseApp>().contentResolver
                    ,imageUri
                )
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val imageDirectory =
                    storage.reference.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInfo(user.copy(imagePath = imageUrl), false)

            }catch (e:Exception){
                viewModelScope.launch {
                    _editInfo.emit(Resource1.Error(e.message.toString()))
                    Log.d("error",e.message.toString())
                }
            }
        }

    }

    fun saveUserInfo(user: User, shouldretrieveOldImage: Boolean) {

        firestore.runTransaction {transaction ->
            val ref = firestore.collection("Users").document(auth.currentUser?.uid.toString())
            if(shouldretrieveOldImage){
                val currentUser = transaction.get(ref).toObject(User::class.java)
                val new_user = user.copy(imagePath = currentUser?.imagePath?: "")
                transaction.set(ref,new_user)
            }else{
                transaction.set(ref,user)
            }
        }
            .addOnSuccessListener {
                viewModelScope.launch {
                    _editInfo.emit(Resource1.Success(user))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _editInfo.emit(Resource1.Error(it.message.toString()))
                }
            }
    }

}