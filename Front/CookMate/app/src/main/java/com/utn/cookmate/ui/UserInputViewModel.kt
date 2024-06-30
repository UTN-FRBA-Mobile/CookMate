package com.utn.cookmate.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utn.cookmate.data.AppStatus
import com.utn.cookmate.data.UserDataUiEvents

class UserInputViewModel : ViewModel() {

    private val _appStatus = MutableLiveData(AppStatus()) // LiveData initialization
    val appStatus: LiveData<AppStatus> = _appStatus // Exposing LiveData

    // Propiedad para acceder a AppStatus de manera segura
    val safeAppStatus: AppStatus
        get() = _appStatus?.value ?: AppStatus()

    fun onEvent(event: UserDataUiEvents) {
        val currentStatus = _appStatus?.value ?: AppStatus()

        when (event) {
            is UserDataUiEvents.EmailEntered -> {
                _appStatus?.value = currentStatus.copy(emailEntered = event.name)
            }
            is UserDataUiEvents.PasswordEntered -> {
                _appStatus?.value = currentStatus.copy(passwordEntered = event.password)
            }
            is UserDataUiEvents.RegisterEmailEntered -> {
                _appStatus?.value = currentStatus.copy(registerEmailEntered = event.registerEmail)
            }
            is UserDataUiEvents.RegisterPasswordEntered -> {
                _appStatus?.value = currentStatus.copy(registerPasswordEntered = event.registerPassword)
            }
            is UserDataUiEvents.RegisterNameEntered -> {
                _appStatus?.value = currentStatus.copy(registerNameEntered = event.registerName)
            }
        }
    }

    fun isValidLoginState(): Boolean {
        return !safeAppStatus.emailEntered.isNullOrEmpty() && !safeAppStatus.passwordEntered.isNullOrEmpty()
    }

    fun isValidRegisterState(): Boolean {
        return !safeAppStatus.registerEmailEntered.isNullOrEmpty() && !safeAppStatus.registerPasswordEntered.isNullOrEmpty() && !safeAppStatus.registerNameEntered.isNullOrEmpty()
    }

    fun isValidBusquedaRecetasState(): Boolean {
        return !safeAppStatus.ingredientesElegidos.isNullOrEmpty()
    }
}
