package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.entity.ScanHistory
import com.dicoding.asclepius.database.local.ScanHistoryDatabase
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.ui.ResultActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private val context: Context) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    val listScanHistory = ArrayList<ScanHistory>()
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ScanHistory>) {
        listScanHistory.clear()
        listScanHistory.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        listScanHistory.removeAt(position)
        notifyItemRemoved(position)
    }

    fun deleteFromDatabase(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = ScanHistoryDatabase.getInstance(context).scanHistoryDao()
            dao.deleteData(id)
        }
    }

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(scanHistory: ScanHistory) {
            val date = Date(scanHistory.time)
            with(binding) {
                shapeableImageView.setImageURI(scanHistory.image.toUri())
                dateTextView.text = dateFormatter.format(date)
                timeTextView.text = timeFormatter.format(date)
                cancerTextView.text = scanHistory.result.drop(11).dropLast(3).trim()
                percentText.text = scanHistory.result.takeLast(3)

                val isSafe = scanHistory.result.contains("Non Cancer")
            percentText.setBackgroundResource(R.drawable.bg_pill_eyebrow)
            if (isSafe) {
                percentText.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#DCFCE7"))
                percentText.setTextColor(Color.parseColor("#15803D"))
            } else {
                percentText.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FEF3C7"))
                percentText.setTextColor(Color.parseColor("#B45309"))
            }

                binding.root.setOnClickListener {
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