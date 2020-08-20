package com.example.firstsignup.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstsignup.Api.RetrofitClient
import com.example.firstsignup.Model.LogInResponse
import com.example.firstsignup.R
import com.example.firstsignup.Storage.SharedPrefManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPassActivity : AppCompatActivity(), View.OnClickListener {
    private var editTextPhone: EditText? = null
    private var editTextOtp: EditText? = null
    private var editTextPassword: EditText? = null
    private var buttonSetPass: Button? = null
    private var buttonGetOtp: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        editTextPhone = findViewById(R.id.editTextForgetPhone)
        buttonGetOtp = findViewById<View>(R.id.buttonGetOtp) as Button
        editTextOtp = findViewById(R.id.editTextForgetOtp)
        editTextPassword = findViewById(R.id.editTextForgetPassword)
        buttonSetPass = findViewById<View>(R.id.buttonSetPass) as Button

        buttonGetOtp!!.setOnClickListener(this)
        buttonSetPass!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view === buttonGetOtp) {
            getOTP()
        }
        else if (view === buttonSetPass) {
            setPass()
        }
    }

    private fun setPass() {
        val phone = editTextPhone!!.text.toString().trim { it <= ' ' }
        val otp= editTextOtp!!.text.toString().trim { it <= ' ' }
        val new_password = editTextPassword!!.text.toString().trim { it <= ' ' }

        if (otp.isEmpty()) {
            editTextOtp!!.error = "OTP is required"
            editTextOtp!!.requestFocus()
            return
        }
        if (new_password.isEmpty()) {
            editTextPassword!!.error = "Password is required"
            editTextPassword!!.requestFocus()
            return
        }

        val call = RetrofitClient
                .instance?.api?.setPass(phone,otp, new_password)

        if (call != null) {
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200){
                        Toast.makeText(applicationContext, "Password Changed", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@ForgetPassActivity, SignInActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    else if (response.code()==400){
                        Toast.makeText(applicationContext, "Otp not valid", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    private fun getOTP() {
        val phone = editTextPhone!!.text.toString().trim { it <= ' ' }

        if (phone.isEmpty()) {
            editTextPhone!!.error = "Mobile number is required"
            editTextPhone!!.requestFocus()
            return
        }

        val call = RetrofitClient
                .instance?.api?.getOTP(phone)

        if (call != null) {
            call.enqueue(object : Callback<ResponseBody> {
//                string s = response.body().tostring()
//                Toast.makeText(applicationContext,s, Toast.LENGTH_LONG).show()
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200){
                        Toast.makeText(applicationContext, "OTP Sent", Toast.LENGTH_LONG).show()
                        editTextOtp?.visibility= View.VISIBLE
                        editTextPassword?.visibility= View.VISIBLE
                        buttonSetPass?.visibility = View.VISIBLE
                        editTextPhone?.isEnabled = false
                        buttonGetOtp?.isEnabled = false
                    }
                    else if (response.code()==400){
                        Toast.makeText(applicationContext, "Number not valid", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }


    }
}
