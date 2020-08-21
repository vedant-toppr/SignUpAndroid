package com.example.firstsignup.ui

//import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstsignup.network.RetrofitClient
import com.example.firstsignup.model.User
import com.example.firstsignup.R
import com.example.firstsignup.storage.SharedPrefManager
import com.example.firstsignup.utils.SignUpMiddleware
import com.example.firstsignup.utils.ValidationHelper

class SignUpActivity : AppCompatActivity(), SignUpMiddleware.MiddleWareListener {

    private var editTextFirstName: EditText? = null
    private var editTextLastName: EditText? = null
    private var editTextUsername: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextPhone: EditText? = null
    private var editTextPassword: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        editTextUsername = findViewById<View>(R.id.edit_text_username) as EditText
        editTextFirstName = findViewById<View>(R.id.edit_text_first_name) as EditText
        editTextLastName = findViewById<View>(R.id.edit_text_last_name) as EditText
        editTextEmail = findViewById<View>(R.id.edit_text_email) as EditText
        editTextPhone = findViewById<View>(R.id.edit_text_phone) as EditText
        editTextPassword = findViewById<View>(R.id.edit_text_password) as EditText
        findViewById<View>(R.id.button_register).setOnClickListener {
            userSignUp()
        }
    }


    private fun userSignUp() {
        val username = editTextUsername!!.text.toString().trim { it <= ' ' }
        val first_name = editTextFirstName!!.text.toString().trim { it <= ' ' }
        val last_name = editTextLastName!!.text.toString().trim { it <= ' ' }
        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }
        val phone = editTextPhone!!.text.toString().trim { it <= ' ' }

        if(editTextFirstName?.let { ValidationHelper.FirstName(it,first_name) } === false){return}
        if(editTextLastName?.let { ValidationHelper.LastName(it,last_name) } ===false){return}
        if(editTextEmail?.let { ValidationHelper.Email(it,email) }===false){return}
        if(editTextPassword?.let { ValidationHelper.Password(it,password) } ===false){return}
        if(editTextPhone?.let { ValidationHelper.Phone(it,phone) }===false){return}
        if(editTextUsername?.let { ValidationHelper.Username(it,username) }===false){return}

        val mMiddleWareLayer = RetrofitClient.instance?.api.let { SignUpMiddleware(it, this) }
        mMiddleWareLayer?.signup(username, first_name, last_name, email, phone, password)
    }

    override fun onSignupSuccess(signupResponse: User?) {
        signupResponse?.let { SharedPrefManager.getInstance(this@SignUpActivity)?.saveUser(it) }
        val intent = Intent(this@SignUpActivity, UserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        showToastMessage("Registered Successfully")
    }

    override fun onSignupFailed(message: String?) {
        showToastMessage(message ?: "")
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}