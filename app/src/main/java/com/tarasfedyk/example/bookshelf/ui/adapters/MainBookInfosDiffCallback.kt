package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import javax.inject.Inject

class MainBookInfosDiffCallback @Inject constructor() : DiffUtil.ItemCallback<BookInfo>() {

    override fun areItemsTheSame(oldItem: BookInfo, newItem: BookInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookInfo, newItem: BookInfo): Boolean {
        return oldItem == newItem
    }
}
