package com.utn.cookmate.data;

sealed class UserDataUiEvents{
    data class UserNameEntered(val name:String) : UserDataUiEvents()
    data class PasswordEntered(val password:String) : UserDataUiEvents()

    data class IngredienteAgregado(val ingredienteAgregado: String) : UserDataUiEvents()
}