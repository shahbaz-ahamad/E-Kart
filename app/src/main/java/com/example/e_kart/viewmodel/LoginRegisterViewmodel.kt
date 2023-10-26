package com.example.e_kart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_kart.datamodel.User
import com.example.e_kart.repository.UserRepo
import com.example.e_kart.util.RegisterFieldSate
import com.example.e_kart.util.RegistrationValidation
import com.example.e_kart.util.Resource
import com.example.e_kart.util.validateEmail
import com.example.e_kart.util.validatePassword
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginRegisterViewmodel @Inject constructor(
    private val repo:UserRepo):ViewModel() {


    private val registrationValidationChannel = Channel<RegisterFieldSate>()
    val registrationValidationFlow : Flow<RegisterFieldSate> = registrationValidationChannel.receiveAsFlow()


    private val _loginFlow= MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow:StateFlow<Resource<FirebaseUser>?> = _loginFlow


    private val _signUpFlow= MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow:StateFlow<Resource<FirebaseUser>?> = _signUpFlow

    private val _resetPassword= MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    val currentUser:FirebaseUser?
        get() = repo.currentUser

    init {
        if(repo.currentUser!=null){
            _loginFlow.value=Resource.Success(repo.currentUser!!)
        }
    }


    fun login(email:String,password:String) = viewModelScope.launch {
        if(checkValidation(email,password)) {
            _loginFlow.value = Resource.Loading
            val result = repo.login(email, password)
            _loginFlow.value = result
        }else{
            val registerFieldSate=RegisterFieldSate(validateEmail(email), validatePassword(password))
            registrationValidationChannel.send(registerFieldSate)
        }
    }



    fun signUp(name:String,email:String,password:String,photo:String?) = viewModelScope.launch {

        if(checkValidation(email, password)) {//if the validation is true then
            _signUpFlow.value = Resource.Loading
            val result = repo.signUp(name, email, password,photo)
            val user = User(name,"",email,photo!!)
            repo.addUserToFirestore(user)
            _signUpFlow.value = result


        }else{//if the validation is not true then
           val registerFieldState=RegisterFieldSate(validateEmail(email), validatePassword(password))
            registrationValidationChannel.send(registerFieldState)
        }
    }

    fun logout(){
        repo.logout()
        _loginFlow.value=null
        _signUpFlow.value=null
    }

    private fun checkValidation(email: String, password: String):Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)
        val shouldRegister =
            emailValidation is RegistrationValidation.Success && passwordValidation is RegistrationValidation.Success

        return shouldRegister
    }

    fun resetPassword(email: String)=viewModelScope.launch {

        try {
            _resetPassword.emit(Resource.Loading)
            repo.resetPassword(email)
            _resetPassword.emit(Resource.Success("Email Sent"))
        }catch (e:Exception){
            // Handle any errors that may occur during password reset

            _resetPassword.emit(Resource.Error(e))
        }

    }
}