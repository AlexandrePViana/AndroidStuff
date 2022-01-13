package com.example.androidprojectocrprice
import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginActivity : AppCompatActivity(){
    private var username = "NULL"
    private lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.login)
        super.onCreate(savedInstanceState)
            auth = Firebase.auth
        var regBtn = findViewById(R.id.regBtn) as Button
        var lgBtn = findViewById(R.id.buttonlogin) as Button
        lgBtn.setOnClickListener {
            doLogin()
        }
        regBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("245675093028-o3jsivavs6l8mr6sa563grm06fqm1gek.apps.googleusercontent.com").requestProfile()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        var sign_in_google = findViewById(R.id.google_login_btn) as Button
        sign_in_google.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            val googleIdToken = account?.idToken ?: ""
            firebaseAuthWithGoogle(googleIdToken)

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Toast.makeText(baseContext, e.statusCode.toString(),Toast.LENGTH_SHORT).show()

        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user,"Google")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null,"null")
                }
            }
    }

    private fun doLogin() {
        var loginMail = findViewById(R.id.loginMail) as EditText
        var loginPassword = findViewById(R.id.loginPassword) as EditText

        if(loginMail.text.toString().isEmpty())
        {
            loginMail.error = "Please enter a email address"
            loginMail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(loginMail.text.toString()).matches())
        {
            loginMail.error = "Please enter a valid email address"
            loginMail.requestFocus()
            return
        }
        if(loginPassword.text.toString().isEmpty())
        {
            loginPassword.error = "Please enter a Password"
            loginPassword.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(loginMail.text.toString(), loginPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user,"Local")
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null,"null")
                }
            }


    }
    @SuppressLint("ShowToast")
    private fun updateUI(currentUser: FirebaseUser?,Or : String?) {
        if(currentUser != null){
            if(currentUser.isEmailVerified)
            {
                username = "Welcome! " + currentUser.displayName.toString()
                Toast.makeText(
                    baseContext,username,
                    Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainApp::class.java))
                finish()
            }else if(Or == "Google")
            {
                username = "Welcome! " + currentUser.displayName.toString()
                Toast.makeText(
                    baseContext,username,
                    Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainApp::class.java))
                finish()

            }
            else{
                Toast.makeText(
                    baseContext,"Please Verify your email",
                    Toast.LENGTH_SHORT).show()
            }

        }
        else
        {
            Toast.makeText(
                baseContext,"Login Failed",
                Toast.LENGTH_SHORT)
        }
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser,"Google")
        }

    }


}