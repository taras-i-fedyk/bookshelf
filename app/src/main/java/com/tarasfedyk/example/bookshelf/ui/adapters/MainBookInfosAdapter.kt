package com.tarasfedyk.example.bookshelf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.R
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import com.tarasfedyk.example.bookshelf.ui.BookInfosFragmentDirections
import com.tarasfedyk.example.bookshelf.ui.adapters.di.qualifiers.BookInfosDiffCallback
import kotlinx.android.synthetic.main.book_info_view.view.*
import javax.inject.Inject

class MainBookInfosAdapter @Inject constructor(
    diffCallback: DiffUtil.ItemCallback<BookMetadata>,
    private val onItemClickListener: OnItemClickListener
): BookInfosAdapter<BookMetadataViewHolder>(diffCallback, onItemClickListener) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookMetadataViewHolder = BookMetadataViewHolder(parent)

    override fun onBindViewHolder(holder: BookMetadataViewHolder, position: Int) {
        val item = getItem(position)
        holder.titleView.text = item!!.title
        holder.itemView.setOnClickListener { onItemClickListener.onItemClicked(item) }
    }
}

class BookMetadataViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.book_info_view, parent, false)
) {
    val titleView: TextView = itemView.title_view
}
