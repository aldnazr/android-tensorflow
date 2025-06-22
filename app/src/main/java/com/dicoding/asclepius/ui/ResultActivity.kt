package com.dicoding.asclepius.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBar.left, systemBar.top, systemBar.right, systemBar.bottom)
            insets
        }

        val intentImageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val intentTextResult = intent.getStringExtra(EXTRA_RESULT)

        with(binding) {
            resultImage.setImageURI(Uri.parse(intentImageUri))
            analisisInput.setText(intentTextResult?.drop(11)?.dropLast(3)?.trim())
            percentageInput.setText(intentTextResult?.takeLast(3))
            toolBar.setNavigationOnClickListener { finish() }
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}