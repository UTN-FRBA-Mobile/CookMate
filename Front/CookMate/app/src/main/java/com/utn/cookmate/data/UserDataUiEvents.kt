package com.utn.cookmate.data;

sealed class UserDataUiEvents{
    data class EmailEntered(val name:String) : UserDataUiEvents()
    data class PasswordEntered(val password:String) : UserDataUiEvents()
}