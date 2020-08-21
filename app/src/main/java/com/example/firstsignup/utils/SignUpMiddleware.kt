package com.example.firstsignup.utils

import android.content.Intent
import android.widget.Toast
import com.example.firstsignup.model.User
import com.example.firstsignup.network.APIService
import com.example.firstsignup.network.RetrofitClient
import com.example.firstsignup.storage.SharedPrefManager
import com.example.firstsignup.ui.UserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SignUpMiddleware(private val apiService: APIService?, private val listener: MiddleWareListener) {

    fun signup(username: String, first_name: String, last_name: String, email: String, phone: String, password: String) {
        apiService?.createUser(username, first_name, last_name, email, phone, password)?.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.code() == 201) {
                    val signupResponse = response.body()
                    listener.onSignupSuccess(signupResponse)
                } else if (response.code() == 400) {
                    try {
                        val s = response.errorBody()!!.string()
                        listener.onSignupFailed(s)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                listener.onSignupFailed(t.message)
            }
        })
    }
    interface MiddleWareListener {
        fun onSignupSuccess(signupResponse: User?)
        fun onSignupFailed(message: String?)
    }
}

