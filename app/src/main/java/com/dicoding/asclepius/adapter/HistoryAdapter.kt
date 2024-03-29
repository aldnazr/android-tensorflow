package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.database.entity.ScanHistory
import com.dicoding.asclepius.database.local.ScanHistoryDatabase
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.ui.ResultActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val listScanHistory = ArrayList<ScanHistory>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ScanHistory>) {
        listScanHistory.clear()
        listScanHistory.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val scanHistoryDatabase by lazy {
            ScanHistoryDatabase.getInstance(itemView.context).scanHistoryDao()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun deleteFavorite(id: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                scanHistoryDatabase.deleteData(id)
                withContext(Dispatchers.Main) {
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(scanHistory: ScanHistory) {
            with(binding) {
                shapeableImageView.setImageURI(Uri.parse(scanHistory.image))
                timeTextView.text = scanHistory.time
                deleteButton.setOnClickListener {
                    deleteFavorite(scanHistory.id)
                }
                materialCard.setOnClickListener {
                    val intent = Intent(itemView.context, ResultActivity::class.java).apply {
                        putExtra(ResultActivity.EXTRA_IMAGE_URI, scanHistory.image)
                        putExtra(ResultActivity.EXTRA_RESULT, scanHistory.result)
                    }
                    itemView.context.startActivity(intent)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return listScanHistory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = listScanHistory[position]
        holder.bind(pos)
    }
}