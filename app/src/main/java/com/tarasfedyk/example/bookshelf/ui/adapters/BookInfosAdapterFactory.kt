package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata

interface BookInfosAdapterFactory {
    fun createBookInfosAdapter(
        diffCallback: DiffUtil.ItemCallback<BookMetadata>,
        onItemClickListener: BookInfosAdapter.OnItemClickListener
    ): BookInfosAdapter<out RecyclerView.ViewHolder>
}
