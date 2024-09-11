package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val splashScreenDuration = 5000L

        // Check if it's the user's first time opening the app
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        Handler(Looper.getMainLooper()).postDelayed({
            when {
                currentUser != null -> {
                    // User is logged in, go to MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                }
                isFirstTime -> {
                    // First time opening the app, go to SignupActivity
                    startActivity(Intent(this, SignUp::class.java))

                    // Update SharedPreferences to mark that the app has been opened
                    with(sharedPreferences.edit()) {
                        putBoolean("isFirstTime", false)
                        apply()
                    }
                }
                else -> {
                    // User not logged in, go to LoginActivity
                    startActivity(Intent(this, Login::class.java))
                }
            }
            finish()
        }, splashScreenDuration)


    }
}