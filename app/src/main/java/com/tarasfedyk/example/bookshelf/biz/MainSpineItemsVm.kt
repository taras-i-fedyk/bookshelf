package com.tarasfedyk.example.bookshelf.biz

import android.util.LruCache
import androidx.annotation.MainThread
import androidx.hilt.lifecycle.ViewModelInject
import com.tarasfedyk.example.bookshelf.biz.models.SpineItem
import com.tarasfedyk.example.bookshelf.biz.utils.enforceMainThread
import kotlinx.coroutines.flow.Flow

class MainSpineItemsVm @ViewModelInject constructor(
    private val booksRepo: BooksRepo
): SpineItemsVm() {

    private val bookIdToFlow: LruCache<String, Flow<List<SpineItem>>> = LruCache(1)

    @MainThread
    override fun getFlow(bookId: String): Flow<List<SpineItem>> {
        enforceMainThread()

        return bookIdToFlow.get(bookId) ?:
        booksRepo.createSpineItemsFlow(bookId)
            .also { bookIdToFlow.put(bookId, it) }
    }
}
