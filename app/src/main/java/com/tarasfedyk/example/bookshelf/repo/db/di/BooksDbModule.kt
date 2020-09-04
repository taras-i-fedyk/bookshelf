package com.tarasfedyk.example.bookshelf.repo.db.di

import android.content.Context
import com.tarasfedyk.example.bookshelf.repo.db.BooksDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object BooksDbModule {

    @Provides
    @Singleton
    fun provideBooksDb(@ApplicationContext appContext: Context): BooksDb = BooksDb(appContext)
}
