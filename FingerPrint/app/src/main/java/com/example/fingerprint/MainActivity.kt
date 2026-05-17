package com.example.fingerprint

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val authenticateButton: Button = findViewById(R.id.authenticate_button)
        val resultTextView: TextView = findViewById(R.id.result_text_view)

        authenticateButton.setOnClickListener {
            promptManager.showBiometricPrompt(
                title = "Biometric Authentication",
                description = "Please authenticate Below"
            )
        }

        lifecycleScope.launch {
            promptManager.promptResults.collectLatest { result ->
                resultTextView.text = when (result) {
                    is BiometricPromptManager.BiometricResult.AuthenticationError -> result.error
                    BiometricPromptManager.BiometricResult.AuthenticationFailed -> "Authentication failed"
                    BiometricPromptManager.BiometricResult.AuthenticationNotSet -> "Authentication not set"
                    BiometricPromptManager.BiometricResult.AuthenticationSuccess -> "Authentication success"
                    BiometricPromptManager.BiometricResult.FeatureUnavailable -> "Feature unavailable"
                    BiometricPromptManager.BiometricResult.HardwareUnavailable -> "Hardware unavailable"
                }
            }
        }
    }
}