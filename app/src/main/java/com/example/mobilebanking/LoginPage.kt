package com.example.mobilebanking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginPage"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        val txtUsername = findViewById<TextInputLayout>(R.id.editText_loginUsername)
        val txtPassword = findViewById<TextInputLayout>(R.id.editText_loginPassword)
        val btnLogin = findViewById<Button>(R.id.button_loginLogin)

        txtPassword.editText?.setTransformationMethod(PasswordTransformationMethod.getInstance())

        txtUsername.editText?.doOnTextChanged { text, start, before, count ->
            if (!txtUsername.error.isNullOrBlank()){
                txtUsername.error = null
            }
        }

        txtPassword.editText?.doOnTextChanged { text, start, before, count ->
            if (!txtPassword.error.isNullOrBlank()){
                txtPassword.error = null
            }
        }

        btnLogin.setOnClickListener {
            val username = txtUsername.editText?.text.toString()
            val password = txtPassword.editText?.text.toString()
//            FirebaseApp.initializeApp(applicationContext)
            if (txtUsername.editText?.text.toString().isEmpty()){
                txtUsername.error = "Username cannot be empty"
            }
            if (txtPassword.editText?.text.toString().isEmpty()){
                txtPassword.error = "Password cannot be emtpy"
            }
            if (
                txtUsername.editText?.text.toString().isNotEmpty() &&
                txtPassword.editText?.text.toString().isNotEmpty()
            ){
                btnLogin.isEnabled = false
                Toast.makeText(baseContext, "Logging in...",
                    Toast.LENGTH_SHORT).show()
                auth = Firebase.auth
                auth.signInWithEmailAndPassword("$username@maebank.com", password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            val intent = Intent(this, MainPage::class.java)
                            intent.putExtra("username", username)
                            intent.putExtra("password", password)
                            startActivity(intent)
                            btnLogin.isEnabled = true
                        } else {
                            btnLogin.isEnabled = true
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

            }

        }

    }
}