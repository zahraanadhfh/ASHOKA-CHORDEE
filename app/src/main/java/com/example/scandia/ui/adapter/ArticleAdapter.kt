package com.example.scandia.ui.adapter;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scandia.databinding.ItemArticleBinding
import com.example.scandia.model.Article
import com.example.scandia.loadImage


class ArticleAdapter(private val itemClick: (Article) -> Unit) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private var items: MutableList<Article> = mutableListOf()

    fun setItems(items: List<Article>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    class ArticleViewHolder(
        private val binding: ItemArticleBinding,
        val itemClick: (Article) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Article) {
            with(item) {
                itemView.setOnClickListener { itemClick(this) }
                binding.run {
                    icPict.loadImage(itemView.context, imgUrl)
                    txtTitle.text = title
                }
            }
        }
    }

}