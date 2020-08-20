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
import com.example.firstsignup.R
import com.example.firstsignup.Storage.SharedPrefManager
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

//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Signing In...")
//        progressDialog.show()

        val call = RetrofitClient
                .instance?.api?.userLogin(username, password)

        if (call != null) {
            call.enqueue(object : Callback<LogInResponse?> {
                override fun onResponse(call: Call<LogInResponse?>, response: Response<LogInResponse?>) {
//                progressDialog.dismiss()
                    if (response.code() == 200) {
                        val logInResponse = response.body()
                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG).show()
                        SharedPrefManager.getInstance(this@SignInActivity)?.saveUser(logInResponse!!.user)
                        val intent = Intent(this@SignInActivity, UserActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Invalid Credentials", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LogInResponse?>, t: Throwable) {
//                progressDialog.dismiss()
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}