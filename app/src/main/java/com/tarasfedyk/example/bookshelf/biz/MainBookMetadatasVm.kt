package com.tarasfedyk.example.bookshelf.biz

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import kotlinx.coroutines.flow.Flow

class MainBookMetadatasVm @ViewModelInject constructor(
    booksRepo: BooksRepo
) : BookMetadatasVm() {

    override val flow: Flow<PagingData<BookMetadata>> =
        booksRepo.createBookMetadatasFlow()
            .cachedIn(viewModelScope)
}
