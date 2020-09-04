package com.tarasfedyk.example.bookshelf.biz

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class SpineFilePathsVm : ViewModel() {
    @MainThread
    abstract fun getFlow(bookId: String): Flow<List<String>>
}
