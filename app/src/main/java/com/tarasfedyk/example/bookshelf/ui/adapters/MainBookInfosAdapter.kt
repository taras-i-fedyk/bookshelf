package com.tarasfedyk.example.bookshelf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.R
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import kotlinx.android.synthetic.main.book_info_view.view.*
import javax.inject.Inject

class MainBookInfosAdapter @Inject constructor(
    diffCallback: DiffUtil.ItemCallback<BookInfo>,
    onItemClickListener: OnItemClickListener
): BookInfosAdapter<BookInfoViewHolder>(diffCallback, onItemClickListener) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookInfoViewHolder = BookInfoViewHolder(parent)

    override fun onBindViewHolder(holder: BookInfoViewHolder, position: Int) {
        val item = getItem(position)
        holder.titleView.text = item!!.title
        holder.itemView.setOnClickListener { onItemClickListener.onItemClicked(item) }
    }
}

class BookInfoViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.book_info_view, parent, false)
) {
    val titleView: TextView = itemView.title_view
}
