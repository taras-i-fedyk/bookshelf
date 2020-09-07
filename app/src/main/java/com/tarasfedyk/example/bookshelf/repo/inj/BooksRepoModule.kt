package com.tarasfedyk.example.bookshelf.repo.inj

import androidx.work.ListenableWorker
import com.tarasfedyk.example.bookshelf.biz.BooksRepo
import com.tarasfedyk.example.bookshelf.repo.DbBooksSaver
import com.tarasfedyk.example.bookshelf.repo.MainBooksRepo
import com.tarasfedyk.example.bookshelf.repo.inj.qualifiers.DbBooksSaverClass
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface BooksRepoModule {

    @Binds
    fun bindBooksRepo(mainBooksRepo: MainBooksRepo): BooksRepo

    companion object {
        @Provides
        @DbBooksSaverClass
        fun provideDbBooksSaverClass(): Class<out ListenableWorker> = DbBooksSaver::class.java
    }
}
