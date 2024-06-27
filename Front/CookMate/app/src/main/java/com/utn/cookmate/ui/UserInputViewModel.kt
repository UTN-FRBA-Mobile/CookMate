package com.utn.cookmate.ui;

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.utn.cookmate.data.AppStatus
import com.utn.cookmate.data.UserDataUiEvents

class UserInputViewModel : ViewModel(){

    var appStatus = mutableStateOf(AppStatus())

    fun onEvent(event: UserDataUiEvents) {
        when(event){
            is UserDataUiEvents.EmailEntered -> {
                appStatus.value = appStatus.value.copy(emailEntered = event.name)
            }

            is UserDataUiEvents.PasswordEntered -> {
                appStatus.value = appStatus.value.copy(passwordEntered = event.password)
            }

            is UserDataUiEvents.RegisterEmailEntered -> {
                appStatus.value = appStatus.value.copy(registerEmailEntered = event.registerEmail)
            }

            is UserDataUiEvents.RegisterPasswordEntered -> {
                appStatus.value = appStatus.value.copy(registerPasswordEntered = event.registerPassword)
            }

            is UserDataUiEvents.RegisterNameEntered -> {
                appStatus.value = appStatus.value.copy(registerNameEntered = event.registerName)
            }
        }
    }

    fun isValidLoginState() : Boolean {   //devuelve Boolean
        return !appStatus.value.emailEntered.isNullOrEmpty() && !appStatus.value.passwordEntered.isNullOrEmpty()
    }

    fun isValidRegisterState() : Boolean {   //devuelve Boolean
        return !appStatus.value.registerEmailEntered.isNullOrEmpty() && !appStatus.value.registerPasswordEntered.isNullOrEmpty() && !appStatus.value.registerNameEntered.isNullOrEmpty()
    }

    fun isValidBusquedaRecetasState() : Boolean {   //devuelve Boolean
        return appStatus.value.ingredientesElegidos.isNotEmpty()
    }

}
