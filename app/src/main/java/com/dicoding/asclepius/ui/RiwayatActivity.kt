package com.dicoding.asclepius.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.databinding.ActivityRiwayatBinding
import com.dicoding.asclepius.model.HistoryModel

class RiwayatActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRiwayatBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<HistoryModel>()
    private val historyAdapter = HistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            binding.appBar.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        with(binding) {
            viewModel.scanHistory.observe(this@RiwayatActivity) {
                historyAdapter.setList(it)
                emptyText.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
            recyclerView.adapter = historyAdapter
            toolBar.setNavigationOnClickListener { finish() }
        }

    }
}