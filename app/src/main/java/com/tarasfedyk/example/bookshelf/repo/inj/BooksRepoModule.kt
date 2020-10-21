package com.tarasfedyk.example.bookshelf.repo.inj

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import androidx.work.ListenableWorker
import com.tarasfedyk.example.bookshelf.biz.BooksRepo
import com.tarasfedyk.example.bookshelf.repo.MainDbBooksFetcher
import com.tarasfedyk.example.bookshelf.repo.MainBooksRepo
import com.tarasfedyk.example.bookshelf.repo.MainDbBooksMediator
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookInfo
import com.tarasfedyk.example.bookshelf.repo.inj.qualifiers.DbBooksFetcherClass
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@ExperimentalPagingApi
@Module
@InstallIn(ApplicationComponent::class)
interface BooksRepoModule {

    @Binds
    fun bindBooksRepo(mainBooksRepo: MainBooksRepo): BooksRepo

    @Binds
    fun bindDbBooksMediator(
        mainDbBooksMediator: MainDbBooksMediator
    ): RemoteMediator<Int, DbBookInfo>

    companion object {
        @Provides
        @DbBooksFetcherClass
        fun provideDbBooksFetcherClass(): Class<out ListenableWorker> =
            MainDbBooksFetcher::class.java
    }
}
