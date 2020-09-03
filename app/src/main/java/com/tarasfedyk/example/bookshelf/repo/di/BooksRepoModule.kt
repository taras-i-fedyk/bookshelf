package com.tarasfedyk.example.bookshelf.repo.di

import android.content.Context
import androidx.work.ListenableWorker
import com.tarasfedyk.example.bookshelf.repo.BooksPreparer
import com.tarasfedyk.example.bookshelf.repo.BooksDir
import com.tarasfedyk.example.bookshelf.repo.di.qualifiers.BooksPreparerClass
import com.tarasfedyk.example.bookshelf.repo.di.qualifiers.BooksDir
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

@Module
@InstallIn(ApplicationComponent::class)
object BooksRepoModule {

    @Provides
    @BooksDir
    fun provideBooksDir(@ApplicationContext appContext: Context): File = BooksDir(appContext)

    @Provides
    @BooksPreparerClass
    fun provideBooksPreparerClass(): Class<out ListenableWorker> = BooksPreparer::class.java
}
