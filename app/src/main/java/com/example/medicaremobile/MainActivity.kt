package com.example.medicaremobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import net.openid.appauth.*
import net.openid.appauth.connectivity.ConnectionBuilder
import java.net.HttpURLConnection
import java.net.URL

class UnsafeConnectionBuilder : ConnectionBuilder {
    companion object {
        val INSTANCE = UnsafeConnectionBuilder()
    }
    override fun openConnection(uri: Uri): HttpURLConnection {
        val url = URL(uri.toString())
        return url.openConnection() as HttpURLConnection
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var authService: AuthorizationService
    private val TAG = "KeycloakDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appAuthConfig = AppAuthConfiguration.Builder()
            .setConnectionBuilder(UnsafeConnectionBuilder.INSTANCE)
            .build()

        authService = AuthorizationService(this, appAuthConfig)

        val btnLogin = findViewById<Button>(R.id.btnLoginKeycloak)
        btnLogin.setOnClickListener {
            startKeycloakLogin()
        }
    }

    private fun startKeycloakLogin() {
        // ⚠️ Thabet mel IP mtaâ el binôme ken tbadlet (Mithal: 192.168.1.151 wala 192.168.0.228)
        val authEndpoint = Uri.parse("http://192.168.0.228:8011/realms/medical-system/protocol/openid-connect/auth")
        val tokenEndpoint = Uri.parse("http://192.168.0.228:8011/realms/medical-system/protocol/openid-connect/token")

        val serviceConfig = AuthorizationServiceConfiguration(authEndpoint, tokenEndpoint)
        val clientId = "medicare-mobile-client"
        val redirectUri = Uri.parse("com.example.medicaremobile://oauth2redirect")

        val request = AuthorizationRequest.Builder(
            serviceConfig,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri
        )
            .setScope("openid profile email")
            .setPrompt("login")
            .build()

        val customTabsIntent = CustomTabsIntent.Builder().build()
        val authIntent = authService.getAuthorizationRequestIntent(request, customTabsIntent)

        try {
            startActivityForResult(authIntent, 100)
            Log.d(TAG, "Intent lancé avec succès vers le navigateur.")
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors du lancement de l'intent: ${e.message}")
            Toast.makeText(this, "Erreur ouverture navigateur : ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null) {
            val resp = AuthorizationResponse.fromIntent(data)
            val ex = AuthorizationException.fromIntent(data)

            if (resp != null) {
                authService.performTokenRequest(resp.createTokenExchangeRequest()) { response, exception ->
                    if (response != null) {
                        val accessToken = response.accessToken
                        runOnUiThread {
                            Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show()
                        }
                        if (accessToken != null) {
                            Log.d(TAG, "Access Token: $accessToken")

                            // 🚀 Houni win n-aba3thou lel HomeActivity w n-passi el Token m3ah
                            val intent = Intent(this, HomeActivity::class.java).apply {
                                putExtra("ACCESS_TOKEN", accessToken)
                            }
                            startActivity(intent)
                            finish() // Bch el user ma y-arja3ch lel login b bouton Retour
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Erreur token : ${exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else if (ex != null) {
                Log.e(TAG, "Erreur Auth: ${ex.message}")
            }
        }
    }
}