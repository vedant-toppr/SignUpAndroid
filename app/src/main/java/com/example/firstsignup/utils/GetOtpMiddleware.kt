package com.example.firstsignup.utils

import android.view.View
import android.widget.Toast
import com.example.firstsignup.model.LogInResponse
import com.example.firstsignup.network.APIService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class GetOtpMiddleware(private val apiService: APIService?, private val listener: MiddleWareListener) {

    fun otpCall(phone: String){
        apiService?.getOTP(phone)?.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200){
                    listener.onOtpSuccess("OTP Sent")
                }
                else if (response.code()==400){
                    listener.onOtpFailed("Number not valid")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                listener.onOtpFailed(t.message)
            }
        })
    }

    interface MiddleWareListener{
        fun onOtpSuccess(message : String?)
        fun onOtpFailed(message: String?)
    }
}