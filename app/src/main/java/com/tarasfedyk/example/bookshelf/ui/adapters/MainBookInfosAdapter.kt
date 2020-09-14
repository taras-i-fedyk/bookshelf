package com.tarasfedyk.example.bookshelf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.R
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import com.tarasfedyk.example.bookshelf.databinding.BookInfoViewBinding
import javax.inject.Inject

class MainBookInfosAdapter @Inject constructor(
    itemsDiffCallback: DiffUtil.ItemCallback<BookInfo>,
    itemClickCallback: BookInfoClickCallback
): BookInfosAdapter<BookInfoViewHolder>(itemsDiffCallback, itemClickCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookInfoViewHolder = BookInfoViewHolder(parent, itemClickCallback)

    override fun onBindViewHolder(holder: BookInfoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }
}

class BookInfoViewHolder(
    parent: ViewGroup,
    private val bookInfoClickCallback: BookInfoClickCallback
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.book_info_view, parent, false)
) {
    private val bookInfoViewBinding = BookInfoViewBinding.bind(itemView)

    fun bind(bookInfo: BookInfo) {
        bookInfoViewBinding.onClickListener = View.OnClickListener {
            bookInfoClickCallback.onBookInfoClicked(bookInfo)
        }
        bookInfoViewBinding.title = bookInfo.title
        bookInfoViewBinding.executePendingBindings()
    }
}
