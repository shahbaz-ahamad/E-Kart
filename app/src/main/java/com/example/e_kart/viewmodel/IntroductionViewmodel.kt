package com.example.e_kart.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.R
import com.example.e_kart.util.Constant.INTRODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewmodel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
) :ViewModel() {

    private val _navigateState = MutableStateFlow(0)
    val navigateState: StateFlow<Int> = _navigateState


    companion object{
        const val SHOPPING_ACTIVITY=23
        val ACCOUNT_OPTION_FRAGMENT= R.id.action_introductionFragment_to_accountOptionFragment
    }
    init {
        val isbuttonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY,false)

        val user =firebaseAuth.currentUser

        if(user!= null){
            //navigate to the shopping activity
            viewModelScope.launch {
                _navigateState.emit(SHOPPING_ACTIVITY)
            }

        }
        else if(isbuttonClicked){

            viewModelScope.launch {
                _navigateState.emit(ACCOUNT_OPTION_FRAGMENT)
            }
        }else{
                Unit
        }
    }


    fun startButtonClicked(){
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY,true).apply()
    }
}