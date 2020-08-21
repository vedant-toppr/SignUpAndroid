package com.example.firstsignup.ui

//import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstsignup.network.RetrofitClient
import com.example.firstsignup.R
import com.example.firstsignup.model.LogInResponse
import com.example.firstsignup.storage.SharedPrefManager
import com.example.firstsignup.utils.SignInMiddleware
import com.example.firstsignup.utils.ValidationHelper

class SignInActivity : AppCompatActivity(), SignInMiddleware.MiddleWareListener {
    private var editTextUsername: EditText? = null
    private var editTextPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        editTextUsername = findViewById(R.id.edit_text_username)
        editTextPassword = findViewById(R.id.edit_text_password)

        findViewById<View>(R.id.button_log_in).setOnClickListener {
            userLogin()
        }
        findViewById<View>(R.id.forget_pass).setOnClickListener {
            startActivity(Intent(this, ForgetPassActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (SharedPrefManager.getInstance(this)?.isLoggedIn!!) {
            val intent = Intent(this, UserActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }


    private fun userLogin() {

        val username = editTextUsername!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }
        if(editTextPassword?.let { ValidationHelper.Password(it,password) } ===false){return}
        if(editTextUsername?.let { ValidationHelper.Username(it,username) }===false){return}

        val mMiddleWareLayer = RetrofitClient.instance?.api.let { SignInMiddleware(it, this) }
        mMiddleWareLayer?.login(username, password)

    }

    override fun onLoginSuccess(loginResponse: LogInResponse?) {
        loginResponse?.user?.let { userData ->
            SharedPrefManager.getInstance(this@SignInActivity)?.saveUser(userData)
        }
        val intent = Intent(this@SignInActivity, UserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        showToastMessage("Login Successful")
    }

    override fun onLoginFailed(message: String?) {
        showToastMessage(message ?: "")
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}