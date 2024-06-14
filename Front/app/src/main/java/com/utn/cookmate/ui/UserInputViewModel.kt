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
                Log.d(TAG, "onEvent:UserNameEntered->>")
                Log.d(TAG, "${appStatus.value}")
            }

            is UserDataUiEvents.PasswordEntered -> {
                appStatus.value = appStatus.value.copy(passwordEntered = event.password)
                Log.d(TAG, "onEvent:PasswordEntered->>")
                Log.d(TAG, "${appStatus.value}")
            }
        }
    }

    fun isValidLoginState() : Boolean {   //devuelve Boolean
        return !appStatus.value.emailEntered.isNullOrEmpty() && !appStatus.value.passwordEntered.isNullOrEmpty()
    }

    fun isValidBusquedaRecetasState() : Boolean {   //devuelve Boolean
        return appStatus.value.ingredientesElegidos.isNotEmpty()
    }

}
