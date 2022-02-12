package com.example.mobilebanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val txtUsername = findViewById<TextInputLayout>(R.id.editText_loginUsername)
        val txtPassword = findViewById<TextInputLayout>(R.id.editText_loginPassword)
        val btnLogin = findViewById<Button>(R.id.button_loginLogin)

        btnLogin.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
            Log.d("Username", txtUsername.editText?.text.toString())
            Log.d("Password", txtPassword.editText?.text.toString())

        }

    }
}