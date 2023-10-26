package com.example.e_kart.repository

import com.example.e_kart.datamodel.Product
import com.example.e_kart.datamodel.User
import com.example.e_kart.util.Resource
import com.example.e_kart.util.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserRepoImpl @Inject constructor(private val firebaseAuth: FirebaseAuth,private val firestore:FirebaseFirestore) : UserRepo {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result= firebaseAuth.signInWithEmailAndPassword(email,password).await()
            Resource.Success(result.user!!)

        }catch (e:Exception){
            Resource.Error(e)
        }

    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
        photo:String?
    ): Resource<FirebaseUser> {
        return try{

            val result=firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            Resource.Success(result.user!!)

        }catch (e:Exception){
            Resource.Error(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }


    override suspend fun fetchSpecialItem(): Resource<List<Product>> {
        return try{

            val querySnapshot=firestore.collection("Product")
                .whereEqualTo("category","Special Product").get().await()

            val sepcialProduct=querySnapshot.toObjects(Product::class.java)//convertin to the onbject
            Resource.Success(sepcialProduct)

        }catch (e:Exception){
            Resource.Error(e)
        }


    }

    override suspend fun fetchBestDealItem(): Resource<List<Product>> {
        return try{
            val querySnapshot =firestore.collection("Product")
                .whereEqualTo("category","Best Deal").get().await()

            val bestDealItem=querySnapshot.toObjects(Product::class.java)
            Resource.Success(bestDealItem)
        }catch (e :Exception){
            Resource.Error(e)
        }
    }

    override suspend fun fetchBestProductItem(): Resource<List<Product>> {
        return try{
            val querySnapshot =firestore.collection("Product")
                .whereEqualTo("category","Best Product").get().await()

            val bestProductItem=querySnapshot.toObjects(Product::class.java)
            Resource.Success(bestProductItem)
        }catch (e :Exception){
            Resource.Error(e)
        }
    }

    override suspend fun addUserToFirestore(user: User): Resource<Unit> {
        return try {
            val userid= currentUser?.uid

            if(userid != null){
                firestore.collection("Users")
                    .document(userid)
                    .set(user).await()
            }
            Resource.Success(Unit)

        }catch (e :Exception){
            Resource.Error(e)
        }
    }
}