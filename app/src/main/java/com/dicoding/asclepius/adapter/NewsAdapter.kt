package com.dicoding.asclepius.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsBinding
import com.dicoding.asclepius.ui.NewsDetail
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class NewsAdapter(private val list: ArrayList<ArticlesItem>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(news: ArticlesItem) {
            Glide.with(itemView.context)
                .load(news.urlToImage!!)
                .into(binding.shapeableImageView)
            binding.materialTextView.text = news.title
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, NewsDetail::class.java).apply {
                    putExtra("title", news.title)
                    putExtra("description", news.description)
                    putExtra("image", news.urlToImage)
                    putExtra("url", news.url)
                }
                itemView.context.startActivity(intent)
            }
            binding.timeTextView.text = "Published at: ${parseDate(news.publishedAt)}"
            binding.moreButton.setOnClickListener {
                showMenu(binding.moreButton, news.url)
            }
        }

        private fun parseDate(date: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val dateParse = inputFormat.parse(date)

            val outputFormat = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault())
            return outputFormat.format(dateParse ?: "No date")
        }

        private fun showMenu(button: MaterialButton, url: String) {
            PopupMenu(itemView.context, button).apply {
                menuInflater.inflate(R.menu.popupmenu, menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menuShare -> {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, url)
                                type = "text/plain"
                            }
                            itemView.context.startActivity(
                                Intent.createChooser(
                                    intent,
                                    "Bagikan"
                                )
                            )
                        }

                        R.id.menuOpen -> {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            itemView.context.startActivity(intent)
                        }
                    }
                    true
                }
                show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = list[position]

        holder.bind(post)
    }
}