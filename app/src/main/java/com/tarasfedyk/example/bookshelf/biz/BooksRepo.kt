package com.tarasfedyk.example.bookshelf.biz

import androidx.paging.PagingData
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import com.tarasfedyk.example.bookshelf.biz.models.SpineItem
import kotlinx.coroutines.flow.Flow

interface BooksRepo {
    fun createBookMetadatasFlow(): Flow<PagingData<BookMetadata>>

    fun createSpineItemsFlow(bookId: String): Flow<List<SpineItem>>
}
