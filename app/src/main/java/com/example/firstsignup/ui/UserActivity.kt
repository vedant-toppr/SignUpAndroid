package com.example.firstsignup.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.firstsignup.R
import com.example.firstsignup.storage.SharedPrefManager

class UserActivity : AppCompatActivity(), View.OnClickListener {
    private var textView: TextView? = null
    private var buttonLogOut: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        textView = findViewById(R.id.textView)
        val user = SharedPrefManager.getInstance(this)?.user
        if (user != null) {
            textView!!.text = "Hello, "+ user.first_name
        }
        buttonLogOut = findViewById(R.id.buttonLogOut)
        buttonLogOut!!.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        if (!SharedPrefManager.getInstance(this)?.isLoggedIn!!) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onClick(view: View) {
        SharedPrefManager.getInstance(this)?.clear()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}