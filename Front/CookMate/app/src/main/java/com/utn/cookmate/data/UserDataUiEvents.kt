package com.utn.cookmate.data;

sealed class UserDataUiEvents{
    data class EmailEntered(val name:String) : UserDataUiEvents()
    data class PasswordEntered(val password:String) : UserDataUiEvents()
    data class RegisterEmailEntered(val registerEmail:String) : UserDataUiEvents()
    data class RegisterPasswordEntered(val registerPassword:String) : UserDataUiEvents()
    data class RegisterNameEntered(val registerName:String) : UserDataUiEvents()
}