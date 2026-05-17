package com.coinflip

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.coinflip.ui.theme.CoinFlipTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CoinFlipTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GifFor3Seconds(
                        onGifFinished = {
                            startActivity(Intent(this, CoinFlip::class.java))
                            finish() // Optional: close splash screen
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GifFor3Seconds(onGifFinished: () -> Unit) {
    val context = LocalContext.current

    var showGif by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        showGif = false
        onGifFinished() // Trigger navigation safely
    }

    if (showGif) {
        val imageLoader = ImageLoader.Builder(context)
            .components {
                if (android.os.Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.startup)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Coin Flip GIF",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(250.dp)
            )
        }
    }
}


