package com.example.firstsignup.utils

import android.util.Patterns
import android.widget.EditText

object ValidationHelper {

    fun FirstName(editTextFirstName: EditText, first_name : String): Boolean {
        if (first_name.isEmpty()) {
            editTextFirstName!!.error = "First Name is required"
            editTextFirstName!!.requestFocus()
            return false
        }
        return true
    }

    fun LastName(editTextLastName: EditText, last_name: String): Boolean{
        if (last_name.isEmpty()) {
            editTextLastName!!.error = "Last Name is required"
            editTextLastName!!.requestFocus()
            return false
        }
        return true
    }

    fun Username(editTextUsername: EditText, username: String): Boolean {
        if (username.isEmpty()) {
            editTextUsername!!.error = "Username is required"
            editTextUsername!!.requestFocus()
            return false
        }
        return true
    }

    fun Password(editTextPassword: EditText, password: String): Boolean {
        if (password.isEmpty()) {
            editTextPassword!!.error = "Password is required"
            editTextPassword!!.requestFocus()
            return false
        }
        return true
    }

    fun Otp(editTextOtp: EditText, otp: String): Boolean {
        if (otp.isEmpty()) {
            editTextOtp!!.error = "Otp is required"
            editTextOtp!!.requestFocus()
            return false
        }
        return true
    }

    fun Phone(editTextPhone: EditText, phone: String ): Boolean {
        if (phone.isEmpty()) {
            editTextPhone!!.error = "Phone is required"
            editTextPhone!!.requestFocus()
            return false
        }
        return true
    }

    fun Email(editTextEmail: EditText, email: String): Boolean {
        if (email.isEmpty()) {
            editTextEmail!!.error = "Email is required"
            editTextEmail!!.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail!!.error = "Email not valid"
            editTextEmail!!.requestFocus()
            return false
        }
        return true
    }


}