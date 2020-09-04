package com.tarasfedyk.example.bookshelf.biz

import android.util.LruCache
import androidx.annotation.MainThread
import androidx.hilt.lifecycle.ViewModelInject
import com.tarasfedyk.example.bookshelf.biz.utils.enforceMainThread
import kotlinx.coroutines.flow.Flow

class MainSpineFilePathsVm @ViewModelInject constructor(
    private val booksRepo: BooksRepo
): SpineFilePathsVm() {

    private val bookIdToFlow: LruCache<String, Flow<List<String>>> = LruCache(1)

    @MainThread
    override fun getFlow(bookId: String): Flow<List<String>> {
        enforceMainThread()

        return bookIdToFlow.get(bookId) ?:
        booksRepo.createSpineFilePathsFlow(bookId)
            .also { bookIdToFlow.put(bookId, it) }
    }
}
