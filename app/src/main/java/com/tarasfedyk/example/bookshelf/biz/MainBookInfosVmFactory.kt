package com.tarasfedyk.example.bookshelf.biz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class MainBookInfosVmFactory @Inject constructor(
    private val booksRepoProvider: Provider<BooksRepo>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainBookInfosVm(booksRepo = booksRepoProvider.get()) as T
}
