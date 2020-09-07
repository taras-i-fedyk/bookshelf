package com.tarasfedyk.example.bookshelf.biz

import androidx.paging.PagingData
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import com.tarasfedyk.example.bookshelf.biz.models.SpineItem
import kotlinx.coroutines.flow.Flow

interface BooksRepo {
    fun createBookInfosFlow(): Flow<PagingData<BookInfo>>

    fun createSpineItemsFlow(bookId: String): Flow<List<SpineItem>>
}
