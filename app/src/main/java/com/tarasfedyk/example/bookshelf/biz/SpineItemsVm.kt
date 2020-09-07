package com.tarasfedyk.example.bookshelf.biz

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import com.tarasfedyk.example.bookshelf.biz.models.SpineItem
import kotlinx.coroutines.flow.Flow

abstract class SpineItemsVm : ViewModel() {
    @MainThread
    abstract fun getFlow(bookId: String): Flow<List<SpineItem>>
}
