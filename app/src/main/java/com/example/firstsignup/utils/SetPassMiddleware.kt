package com.example.firstsignup.utils

import android.content.Intent
import android.widget.Toast
import com.example.firstsignup.network.APIService
import com.example.firstsignup.ui.SignInActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class SetPassMiddleware(private val apiService: APIService?, private val listener: MiddleWareListener) {

    fun setPass(phone: String, otp: String, new_password: String){
        apiService?.setPass(phone, otp, new_password)?.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    listener.onSetPassSuccess("Password Changed")
                } else if (response.code() == 400) {
                    listener.onSetPassFailed("OTP not Valid")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener.onSetPassFailed(t.message)
            }
        })
    }

    interface MiddleWareListener{
        fun onSetPassSuccess(message : String?)
        fun onSetPassFailed(message: String?)
    }
}