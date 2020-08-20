package com.example.firstsignup.Utils

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.firstsignup.Activity.SignInActivity
import com.example.firstsignup.Activity.UserActivity
import com.example.firstsignup.Api.RetrofitClient
import com.example.firstsignup.Model.LogInResponse
import com.example.firstsignup.Model.User
import com.example.firstsignup.Storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MiddleWareLayer (private val retrofitClient: RetrofitClient, private val listener: MiddleWareListener){

    fun login(username : String, password: String){
        val call = RetrofitClient
                .instance?.api?.userLogin(username, password)

        if (call != null) {
            call.enqueue(object : Callback<LogInResponse?> {
                override fun onResponse(call: Call<LogInResponse?>, response: Response<LogInResponse?>) {
                    if (response.code() == 200) {
                        val logInResponse = response.body()
                        listener.onLoginSuccess("Login Successfull", logInResponse!!.user)


//                        Toast.makeText(SignInActivity, "Login Successful", Toast.LENGTH_SHORT).show()
//                        SharedPrefManager.getInstance(SignInActivity)?.saveUser(logInResponse!!.user)
//                        val intent = Intent(SignInActivity, UserActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)


                    } else {

                        listener.onLoginFailed("Invalid Credentials")
//                        Toast.makeText(SignInActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LogInResponse?>, t: Throwable) {
//                progressDialog.dismiss()
                    Toast.makeText(SignInActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    interface MiddleWareListener {
        fun onLoginSuccess(loginMessage:String, user: User)
        fun onLoginFailed(errorMessage: String)
    }
}