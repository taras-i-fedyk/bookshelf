package com.tarasfedyk.example.bookshelf.ui.adapters

import com.tarasfedyk.example.bookshelf.biz.models.BookInfo

fun interface BookInfoClickCallback {
    fun onBookInfoClicked(bookInfo: BookInfo)
}
