package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: MaterialButton
    private lateinit var loginTextView: TextView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        nameEditText = findViewById(R.id.name)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        signUpButton = findViewById(R.id.signUpButton)
        loginTextView = findViewById(R.id.txtLogin)
        progressBar = findViewById(R.id.progressBar)



        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            } else {
                checkEmail()
                progressBar.visibility = ProgressBar.VISIBLE

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                createUser(email, name, userId)
                            } else {
                                progressBar.visibility = ProgressBar.GONE
                                Toast.makeText(this, "Failed to get user ID", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            progressBar.visibility = ProgressBar.GONE
                            Toast.makeText(this, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }




        loginTextView.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun createUser(email: String, name: String, uid: String) {
        val userMap = HashMap<String, String>()
        userMap["email"] = email
        userMap["name"] = name
        userMap["uid"] = uid

        database.child(uid).setValue(userMap).addOnCompleteListener { task ->
            progressBar.visibility = ProgressBar.GONE
            if (task.isSuccessful) {
                Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^((?!\\.)[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:(?!-)" +
                "[a-zA-Z0-9-]+(?:(?<!-)[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,})$"
        return email.matches(emailRegex.toRegex())
    }


    private fun checkEmail(){

        val email = emailEditText.text.toString().trim()
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {

                    } else {
                        progressBar.visibility = ProgressBar.GONE
                        Toast.makeText(this, "Email is already in use. Please use a different email.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    progressBar.visibility = ProgressBar.GONE
                    Toast.makeText(this, "Failed to check email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }

}
