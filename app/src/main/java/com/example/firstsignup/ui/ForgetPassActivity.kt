package com.example.firstsignup.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstsignup.network.RetrofitClient
import com.example.firstsignup.R
import com.example.firstsignup.utils.GetOtpMiddleware
import com.example.firstsignup.utils.SetPassMiddleware
import com.example.firstsignup.utils.ValidationHelper

class ForgetPassActivity : AppCompatActivity(), GetOtpMiddleware.MiddleWareListener, SetPassMiddleware.MiddleWareListener{
    private var editTextPhone: EditText? = null
    private var editTextOtp: EditText? = null
    private var editTextPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        editTextPhone = findViewById(R.id.edit_text_forget_phone)
        editTextOtp = findViewById(R.id.edit_text_forget_otp)
        editTextPassword = findViewById(R.id.edit_text_forget_password)
        findViewById<View>(R.id.button_set_pass).setOnClickListener {
            setPass()
        }
        findViewById<View>(R.id.button_get_otp).setOnClickListener {
            getOTP()
        }
    }

    private fun setPass() {
        val phone = editTextPhone!!.text.toString().trim { it <= ' ' }
        val otp= editTextOtp!!.text.toString().trim { it <= ' ' }
        val new_password = editTextPassword!!.text.toString().trim { it <= ' ' }

        if(editTextPassword?.let { ValidationHelper.Password(it,new_password) } ===false){return}
        if(editTextOtp?.let { ValidationHelper.Otp(it,otp) } ===false){return}

        val mMiddleWareLayer = RetrofitClient.instance?.api.let { SetPassMiddleware(it, this) }
        mMiddleWareLayer?.setPass(phone, otp, new_password)

    }

    private fun getOTP() {
        val phone = editTextPhone!!.text.toString().trim { it <= ' ' }
        if(editTextPhone?.let { ValidationHelper.Phone(it,phone) }===false){return}

        val mMiddleWareLayer = RetrofitClient.instance?.api.let { GetOtpMiddleware(it, this) }
        mMiddleWareLayer?.otpCall(phone)

    }
    override fun onOtpSuccess(message: String?) {
        editTextOtp?.visibility= View.VISIBLE
        editTextPassword?.visibility= View.VISIBLE
        findViewById<View>(R.id.button_set_pass).visibility = View.VISIBLE
        editTextPhone?.isEnabled = false
        findViewById<View>(R.id.button_get_otp).isEnabled = false
        showToastMessage(message ?: "")
    }

    override fun onOtpFailed(message: String?) {
        showToastMessage(message ?: "")
    }

    override fun onSetPassSuccess(message: String?){
        val intent = Intent(this@ForgetPassActivity, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        showToastMessage(message ?: "")
    }
    override fun onSetPassFailed(message: String?){
        showToastMessage(message ?: "")
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
