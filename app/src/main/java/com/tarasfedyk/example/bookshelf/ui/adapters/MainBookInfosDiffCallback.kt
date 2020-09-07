package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import javax.inject.Inject

class MainBookInfosDiffCallback @Inject constructor() : DiffUtil.ItemCallback<BookMetadata>() {

    override fun areItemsTheSame(oldItem: BookMetadata, newItem: BookMetadata): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookMetadata, newItem: BookMetadata): Boolean {
        return oldItem == newItem
    }
}
