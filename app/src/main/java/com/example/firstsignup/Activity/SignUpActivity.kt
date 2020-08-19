package com.example.firstsignup.Activity

//import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstsignup.Api.RetrofitClient
import com.example.firstsignup.Model.User
import com.example.firstsignup.R
import com.example.firstsignup.Storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    private var buttonSignUp: Button? = null
    private var editTextFirstName: EditText? = null
    private var editTextLastName: EditText? = null
    private var editTextUsername: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        buttonSignUp = findViewById<View>(R.id.buttonRegister) as Button
        editTextUsername = findViewById<View>(R.id.editTextUsername) as EditText
        editTextFirstName = findViewById<View>(R.id.editTextFirstName) as EditText
        editTextLastName = findViewById<View>(R.id.editTextLastName) as EditText
        editTextEmail = findViewById<View>(R.id.editTextEmail) as EditText
        editTextPassword = findViewById<View>(R.id.editTextPassword) as EditText
        buttonSignUp!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view === buttonSignUp) {
            userSignUp()
        }
    }

    private fun userSignUp() {
        val username = editTextUsername!!.text.toString().trim { it <= ' ' }
        val first_name = editTextFirstName!!.text.toString().trim { it <= ' ' }
        val last_name = editTextLastName!!.text.toString().trim { it <= ' ' }
        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            editTextEmail!!.error = "Email is required"
            editTextEmail!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail!!.error = "Email not valid"
            editTextEmail!!.requestFocus()
            return
        }
        if (first_name.isEmpty()) {
            editTextFirstName!!.error = "First Name is required"
            editTextFirstName!!.requestFocus()
            return
        }
        if (last_name.isEmpty()) {
            editTextLastName!!.error = "Last Name is required"
            editTextLastName!!.requestFocus()
            return
        }
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
//        progressDialog.setMessage("Signing Up...")
//        progressDialog.show()
        val call = RetrofitClient
                .instance
                ?.api
                ?.createUser(
                        username,
                        first_name,
                        last_name,
                        email,
                        password)
        if (call != null) {
            call.enqueue(object : Callback<User?> {
                override fun onResponse(call: Call<User?>, response: Response<User?>) {
//                progressDialog.dismiss()
                    if (response.code() == 201) {
                        Toast.makeText(applicationContext, "Registered Successfully", Toast.LENGTH_LONG).show()
                        response.body()?.let { SharedPrefManager.getInstance(this@SignUpActivity)?.saveUser(it) }
                        val intent = Intent(this@SignUpActivity, UserActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else if (response.code() == 400) {
                        try {
                            val s = response.errorBody()!!.string()
                            Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
//                progressDialog.dismiss()
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}