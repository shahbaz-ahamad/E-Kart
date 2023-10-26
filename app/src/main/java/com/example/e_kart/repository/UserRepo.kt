package com.example.e_kart.repository

import com.example.e_kart.datamodel.Product
import com.example.e_kart.datamodel.User
import com.example.e_kart.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

interface UserRepo {
    val currentUser: FirebaseUser? //user can be null
    suspend fun login(email:String,password:String): Resource<FirebaseUser>
    suspend fun signUp(name:String,email:String,password:String,photo:String?): Resource<FirebaseUser>
    fun logout()
    suspend fun resetPassword(email: String)
    suspend fun fetchSpecialItem():Resource<List<Product>>
    suspend fun fetchBestDealItem():Resource<List<Product>>
    suspend fun fetchBestProductItem():Resource<List<Product>>
    suspend fun addUserToFirestore(user: User): Resource<Unit>
}