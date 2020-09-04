package com.tarasfedyk.example.bookshelf.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.R
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import kotlinx.android.synthetic.main.book_metadata_view.view.*
import javax.inject.Inject

class BookMetadatasAdapter @Inject constructor(
) : PagingDataAdapter<BookMetadata, BookMetadataViewHolder>(BookMetadatasComparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookMetadataViewHolder = BookMetadataViewHolder(parent)

    override fun onBindViewHolder(holder: BookMetadataViewHolder, position: Int) {
        val item = getItem(position)
        holder.titleView.text = item?.title
    }
}

object BookMetadatasComparator : DiffUtil.ItemCallback<BookMetadata>() {
    override fun areItemsTheSame(oldItem: BookMetadata, newItem: BookMetadata): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookMetadata, newItem: BookMetadata): Boolean {
        return oldItem == newItem
    }
}

class BookMetadataViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.book_metadata_view, parent, false)
) {
    val titleView: TextView = itemView.title_view
}
