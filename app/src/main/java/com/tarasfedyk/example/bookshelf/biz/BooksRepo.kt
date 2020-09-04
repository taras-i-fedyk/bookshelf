package com.tarasfedyk.example.bookshelf.biz

import androidx.paging.PagingData
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import kotlinx.coroutines.flow.Flow

interface BooksRepo {
    fun createBookMetadatasFlow(): Flow<PagingData<BookMetadata>>

    fun createSpineFilePathsFlow(bookId: String): Flow<List<String>>
}
