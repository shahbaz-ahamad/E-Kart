package com.example.e_kart.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_kart.datamodel.Category
import com.example.e_kart.viewmodel.CategoryViewmodel
import com.google.firebase.firestore.FirebaseFirestore

class BaseCategoryViewmodelFactory(
    private val firestore: FirebaseFirestore,
    private val category: Category
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewmodel::class.java)) {
            return CategoryViewmodel(firestore, category) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}