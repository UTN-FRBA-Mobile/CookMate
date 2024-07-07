package com.utn.cookmate.ui;

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASSWORD = "password"
    private val FIREBASE_TOKEN = "FIREBASE_TOKEN"
    private const val IMAGE_SAVE = "image_save"

    fun getFirebaseToken(context: Context): String? {
        return getPreferences(context).getString(FIREBASE_TOKEN, null)
    }

    fun setFirebaseToken(context: Context, token: String) {
        val editor = getPreferencesEditor(context)
        editor.putString(FIREBASE_TOKEN, token)
        editor.apply()
    }

    fun getPreferencesEditor(context: Context): SharedPreferences.Editor {
        return getPreferences(context).edit()
    }

    fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveLoginDetails(context: Context, email: String, password: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    fun imageLocal(context: Context, imageDownload : MutableMap<String,ByteArray>) {
        val editor = getPreferences(context).edit()
        editor.putString(IMAGE_SAVE, imageDownload.toString())
        editor.apply()
    }

    fun getLoginDetails(context: Context): Pair<String?, String?> {
        val prefs = getPreferences(context)
        val email = prefs.getString(KEY_EMAIL, null)
        val password = prefs.getString(KEY_PASSWORD, null)
        return Pair(email, password)
    }

    fun getLoginDetails(context: Context, userInputViewModel: UserInputViewModel){
        val prefs = getPreferences(context)
        val mutableState : MutableMap<String,ByteArray>

        //userInputViewModel.appStatus.value.imagenesDescargadas.putAll(prefs.getString(IMAGE_SAVE,null))
        val password = prefs.getString(KEY_PASSWORD, null)
    }

    fun clearLoginDetails(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear()
        editor.apply()
    }


}
