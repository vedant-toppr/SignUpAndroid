package com.example.firstsignup.storage

import android.content.Context
import com.example.firstsignup.model.User

class SharedPrefManager private constructor(private val mCtx: Context) {
    fun saveUser(user: User) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", user.username)
        editor.putString("first_name", user.first_name)
        editor.putString("last_name", user.last_name)
        editor.putString("email", user.email)
        editor.apply()
    }

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("username", null) != null
        }
    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                    sharedPreferences.getString("username", null)!!,
                    sharedPreferences.getString("first_name", null)!!,
                    sharedPreferences.getString("last_name", null)!!,
                    sharedPreferences.getString("email", null)!!
            )
        }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val SHARED_PREF_NAME = "my_shared_pref"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager? {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance
        }
    }
}