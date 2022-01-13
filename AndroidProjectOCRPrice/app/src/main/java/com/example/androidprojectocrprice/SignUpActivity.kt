package com.example.androidprojectocrprice

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import java.io.ByteArrayOutputStream


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        var backToLogin = findViewById(R.id.backToLogin) as TextView
        var buttonreg = findViewById(R.id.buttonreg) as Button

        backToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        auth = FirebaseAuth.getInstance()
        buttonreg.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        var regemail = findViewById(R.id.regemail) as TextView
        var regpw = findViewById(R.id.regpw) as TextView
        var regname = findViewById(R.id.regname) as TextView


        if (regemail.text.toString().isEmpty()) {
            regemail.error = "Please enter a email address"
            regemail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(regemail.text.toString()).matches()) {
            regemail.error = "Please enter a valid email address"
            regemail.requestFocus()
            return
        }
        if (regpw.text.toString().isEmpty()) {
            regpw.error = "Please enter a Password"
            regpw.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(regemail.text.toString(), regpw.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userc = auth.currentUser
                    userc!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                userc.updateProfile(userProfileChangeRequest {
                                    displayName = regname.text.toString()
                                }).addOnCompleteListener {
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            baseContext, "Sign up Complete!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    }
                                }

                            }
                        }
                }

            }
    }
}