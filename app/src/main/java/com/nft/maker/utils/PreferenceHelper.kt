package com.nft.maker.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceHelper @Inject constructor(@ApplicationContext context: Context) {

    private val appPrefs: SharedPreferences =
        context.getSharedPreferences(Constants.DB_NAME, Context.MODE_PRIVATE)

    object PreferenceVariable {
        const val AUTH_TOKEN = "auth_token"
        const val USER_EMAIL = "user_email"
        const val USER_ID = "user_id"
        const val ACCOUNT_ID = "account_id"
        const val Full_NAME = "full_name"
    }

    private val editor: SharedPreferences.Editor = appPrefs.edit()


    fun setOnChange(listener: OnSharedPreferenceChangeListener?) {
        appPrefs.registerOnSharedPreferenceChangeListener(listener)
    }

    // Auth Token Setter/Getter
    var authToken: String
        get() = appPrefs.getString(PreferenceVariable.AUTH_TOKEN, "").toString()
        set(authToken) {
            editor.putString(PreferenceVariable.AUTH_TOKEN, authToken)
            editor.apply()
        }

    // USER EMAIL Setter/Getter
    var userEmail: String
        get() = appPrefs.getString(PreferenceVariable.USER_EMAIL, "").toString()
        set(email) {
            editor.putString(PreferenceVariable.USER_EMAIL, email)
            editor.apply()
        }

    var userId: Int
        get() = appPrefs.getInt(PreferenceVariable.USER_ID, 0)
        set(userId) {
            editor.putInt(PreferenceVariable.USER_ID, userId)
            editor.apply()
        }

    // USER PASSWORD Setter/Getter
    var accountID: String
        get() = appPrefs.getString(PreferenceVariable.ACCOUNT_ID, "").toString()
        set(accountID) {
            editor.putString(PreferenceVariable.ACCOUNT_ID, accountID)
            editor.apply()
        }
    var fullName: String
        get() = appPrefs.getString(PreferenceVariable.Full_NAME, "").toString()
        set(fullName) {
            editor.putString(PreferenceVariable.Full_NAME, fullName)
            editor.apply()
        }


    init {
        editor.apply()
    }
}