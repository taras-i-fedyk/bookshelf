package com.tarasfedyk.example.bookshelf.ui.adapters

import android.view.View
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata

abstract class BookInfosAdapter<VH : RecyclerView.ViewHolder> (
    diffCallback: DiffUtil.ItemCallback<BookMetadata>,
    onItemClickListener: OnItemClickListener
): PagingDataAdapter<BookMetadata, VH>(diffCallback) {

    fun interface OnItemClickListener {
        fun onItemClicked(item: BookMetadata)
    }
}
