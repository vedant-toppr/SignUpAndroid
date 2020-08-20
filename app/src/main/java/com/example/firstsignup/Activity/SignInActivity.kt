package com.example.firstsignup.Activity

//import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstsignup.Api.RetrofitClient
import com.example.firstsignup.Model.LogInResponse
import com.example.firstsignup.Model.User
import com.example.firstsignup.R
import com.example.firstsignup.Storage.SharedPrefManager
import com.example.firstsignup.Utils.MiddleWareLayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private var editTextUsername: EditText? = null
    private var editTextPassword: EditText? = null
    private var buttonSignIn: Button? = null
    private var forgetPass: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignIn = findViewById<View>(R.id.buttonLogIn) as Button
        forgetPass = findViewById<View>(R.id.forgetPass) as TextView
        buttonSignIn!!.setOnClickListener(this)
        forgetPass!!.setOnClickListener(this)
//        findViewById<View>(R.id.buttonLogIn).setOnClickListener(this)
//        findViewById<View>(R.id.forgetPass).setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        if (SharedPrefManager.getInstance(this)?.isLoggedIn!!) {
            val intent = Intent(this, UserActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onClick(view: View) {
        if (view === buttonSignIn) {
            userLogin()
        }
        else if (view === forgetPass) {
            startActivity(Intent(this, ForgetPassActivity::class.java))
        }
    }

    private fun userLogin() {

        val username = editTextUsername!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }

        if (username.isEmpty()) {
            editTextUsername!!.error = "Username is required"
            editTextUsername!!.requestFocus()
            return
        }
        if (password.isEmpty()) {
            editTextPassword!!.error = "Password is required"
            editTextPassword!!.requestFocus()
            return
        }
        val listener :MiddleWareLayer.MiddleWareListener
        val mMiddleWareLayer : MiddleWareLayer? = RetrofitClient.instance?.let {MiddleWareLayer(it,listener)}
        mMiddleWareLayer?.login(username, password)
        
    }

    override fun onLoginSuccess(loginMessage: String, user: User){
        Toast.makeText(this, loginMessage, Toast.LENGTH_SHORT).show()
        SharedPrefManager.getInstance(this@SignInActivity)?.saveUser(user)
        val intent = Intent(this@SignInActivity, UserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onLoginFailed(errorMessage: String){
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}