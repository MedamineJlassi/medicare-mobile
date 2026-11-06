package com.example.medicaremobile

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.jwt.JWT

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val accessToken = intent.getStringExtra("ACCESS_TOKEN")

        if (accessToken != null) {
            try {
                // Decode el JWT Token
                val jwt = JWT(accessToken)

                // Récupération ta3 el username wala email
                val username = jwt.getClaim("preferred_username").asString() ?: "Utilisateur"
                val email = jwt.getClaim("email").asString() ?: ""

                Log.d("HomeDebug", "Username: $username, Email: $email")

                // Affichage fel UI
                tvWelcome.text = "Bienvenue, $username !"

            } catch (e: Exception) {
                Log.e("HomeDebug", "Erreur lors du décodage du token: ${e.message}")
                tvWelcome.text = "Bienvenue dans l'Espace Patient"
            }
        }
    }
}