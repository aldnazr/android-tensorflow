package com.dicoding.asclepius.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.model.NewsModel

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<NewsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.appBar.setPadding(0, systemBar.top, 0, 0)
            v.setPadding(0, 0, 0, systemBar.bottom)
            insets
        }

        with(binding) {
            val divider = DividerItemDecoration(this@MainActivity, RecyclerView.VERTICAL)
            recyclerView.addItemDecoration(divider)
            recyclerView.setOnScrollChangeListener { _, _, i2, _, i4 ->
                if (i2 > i4) binding.fab.shrink() else binding.fab.extend()
            }
            fab.setOnClickListener {
                startActivity(Intent(this@MainActivity, ScanActivity::class.java))
            }
            viewModel.response.observe(this@MainActivity) {
                it.removeIf { article ->
                    article.title == "[Removed]" || article.urlToImage == null
                }
                recyclerView.adapter = NewsAdapter(it)
            }
            viewModel.isLoading.observe(this@MainActivity) {
                progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }
}