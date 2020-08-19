package com.example.firstsignup.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.firstsignup.R
import com.example.firstsignup.Storage.SharedPrefManager

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var buttonSignIn: Button? = null
    private var buttonSignUp: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonSignIn = findViewById<View>(R.id.buttonSignIn) as Button
        buttonSignUp = findViewById<View>(R.id.buttonSignUp) as Button
        buttonSignIn!!.setOnClickListener(this)
        buttonSignUp!!.setOnClickListener(this)
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
            startActivity(Intent(this, SignInActivity::class.java))
        } else if (view === buttonSignUp) {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}