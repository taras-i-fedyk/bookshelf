package com.tarasfedyk.example.bookshelf.biz

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import kotlinx.coroutines.flow.Flow

class MainBookInfosVm @ViewModelInject constructor(
    booksRepo: BooksRepo
) : BookInfosVm() {

    override val flow: Flow<PagingData<BookInfo>> =
        booksRepo.createBookInfosFlow()
            .cachedIn(viewModelScope)
}
