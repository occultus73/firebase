package io.github.occultus73.firebase.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import io.github.occultus73.firebase.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (firebaseAuth.currentUser != null) openHomeActivity()

        login.setOnClickListener {
            //read given login details
            val email = username.text.toString().trim()
            val password = password.text.toString().trim()
            if(email.isBlank() || password.isBlank()){
                makeToast(getString(R.string.request_login))
                return@setOnClickListener
            }

            //attempt to login with details
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { openHomeActivity() }
                .addOnFailureListener {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { openHomeActivity() }
                        .addOnFailureListener { makeToast(it.message.toString()) }
                }
        }

    }

    private fun makeToast(text: String) {
        //"Please verify email - link sent"
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun openHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }
}