package com.coinflip

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.random.Random

class CoinFlip : AppCompatActivity() {

    private lateinit var coinImageView: ImageView
    private lateinit var resultTextView: TextView
    private lateinit var headsInput: EditText
    private lateinit var tailsInput: EditText
    private lateinit var themeSwitch: SwitchMaterial

    private val handler = Handler(Looper.getMainLooper())
    private var flipCount = 0
    private val totalFlips = 10
    private var delay = 50L
    private var finalResultRes: Int = R.drawable.heads

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_flip)

        coinImageView    = findViewById(R.id.coinImageView)
        resultTextView   = findViewById(R.id.resultTextView)
        headsInput       = findViewById(R.id.headsLabelInput)
        tailsInput       = findViewById(R.id.tailsLabelInput)
        themeSwitch      = findViewById(R.id.themeSwitch)

        // Set initial switch state from current mode
        themeSwitch.isChecked = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO

            AppCompatDelegate.setDefaultNightMode(mode)
            recreate()   // ← force the activity to re-load under the new theme
        }

        // push camera back so you never see a mirror state
        coinImageView.cameraDistance = 20_000 * resources.displayMetrics.density

        coinImageView.setOnClickListener {
            flipCount = 0
            delay = 50L
            resultTextView.text = ""
            finalResultRes = if (Random.nextBoolean()) R.drawable.heads else R.drawable.tails
            startFlipCycle()
        }
    }

    private fun startFlipCycle() {
        val isLastFlip = flipCount == totalFlips - 1
        val imageRes = if (!isLastFlip) {
            if (flipCount % 2 == 0) R.drawable.heads else R.drawable.tails
        } else {
            finalResultRes
        }
        val label = when (imageRes) {
            R.drawable.heads -> headsInput.text.toString().ifBlank { "HEADS" }
            else              -> tailsInput.text.toString().ifBlank { "TAILS" }
        }

        coinImageView.animate()
            .rotationY(90f)
            .setDuration(delay / 2)
            .withEndAction {
                coinImageView.setImageResource(imageRes)
                coinImageView.rotationY = -90f
                resultTextView.text = label

                coinImageView.animate()
                    .rotationY(0f)
                    .setDuration(delay / 2)
                    .withEndAction {
                        flipCount++
                        delay += 30L
                        if (flipCount < totalFlips) {
                            handler.postDelayed({ startFlipCycle() }, delay)
                        }
                    }
                    .start()
            }
            .start()
    }
}
