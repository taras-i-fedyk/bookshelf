package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import javax.inject.Inject

class MainBookInfosAdapterFactory @Inject constructor(): BookInfosAdapterFactory {

    override fun createBookInfosAdapter(
        itemsDiffCallback: DiffUtil.ItemCallback<BookInfo>,
        itemClickCallback: BookInfoClickCallback
    ): BookInfosAdapter<out RecyclerView.ViewHolder> =
        MainBookInfosAdapter(itemsDiffCallback, itemClickCallback)
}
