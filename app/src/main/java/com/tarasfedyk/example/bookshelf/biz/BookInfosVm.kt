package com.tarasfedyk.example.bookshelf.biz

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import kotlinx.coroutines.flow.Flow

abstract class BookInfosVm : ViewModel() {
    abstract val flow: Flow<PagingData<BookMetadata>>
}
