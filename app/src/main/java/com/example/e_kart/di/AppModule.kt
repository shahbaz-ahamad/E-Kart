package com.example.e_kart.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.e_kart.Firebase.FirebaseDB
import com.example.e_kart.repository.UserRepo
import com.example.e_kart.repository.UserRepoImpl
import com.example.e_kart.util.Constant.SHARED_PREFERENCE_INTRODUCTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth=FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository( impl: UserRepoImpl):UserRepo=impl

    @Provides
    fun provideFirebaseFirestore():FirebaseFirestore =Firebase.firestore

    @Provides
    fun provideIntroductionSharedPrefernces(
        application: Application
    )= application.getSharedPreferences(SHARED_PREFERENCE_INTRODUCTION,MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFirebaseDB(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ) = FirebaseDB(firestore,firebaseAuth)

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}