package com.example.e_kart.util

sealed class RegistrationValidation(){
    object Success :RegistrationValidation()
    data class  Failed(val msg:String):RegistrationValidation()
}

data class RegisterFieldSate(
    val email:RegistrationValidation,
    val password:RegistrationValidation
)
