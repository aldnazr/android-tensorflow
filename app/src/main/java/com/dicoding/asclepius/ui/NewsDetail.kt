package com.dicoding.asclepius.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.ActivityNewsDetailBinding

class NewsDetail : AppCompatActivity() {

    private val binding by lazy { ActivityNewsDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0, systemBars.bottom)
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        with(binding) {
            Glide.with(this@NewsDetail)
                .load(intent.getStringExtra("image"))
                .into(imageView)
            titleTextView.text = intent.getStringExtra("title")
            descTextView.text = intent.getStringExtra("description")
            buttonBack.setOnClickListener { finish() }
            buttonReadArticle.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(intent.getStringExtra("url"))
                    )
                )
            }
        }
    }
}