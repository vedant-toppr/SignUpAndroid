package com.example.firstsignup.utils

import com.example.firstsignup.model.LogInResponse
import com.example.firstsignup.network.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInMiddleware(private val apiService: APIService?, private val listener: MiddleWareListener) {

    fun login(username: String, password: String) {
        apiService?.userLogin(username, password)?.enqueue(object : Callback<LogInResponse?> {
            override fun onResponse(call: Call<LogInResponse?>, response: Response<LogInResponse?>) {
                if (response.code() == 200) {
                    val logInResponse = response.body()
                    listener.onLoginSuccess(logInResponse)
                } else {
                    listener.onLoginFailed(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<LogInResponse?>, t: Throwable) {
                listener.onLoginFailed(t.message)
            }
        })
    }

    interface MiddleWareListener {
        fun onLoginSuccess(loginResponse: LogInResponse?)
        fun onLoginFailed(message: String?)
    }
}