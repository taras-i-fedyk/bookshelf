package com.tarasfedyk.example.bookshelf.repo.raw.di

import com.tarasfedyk.example.bookshelf.repo.raw.BookParser
import com.tarasfedyk.example.bookshelf.repo.raw.MainBookParser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface BooksRawModule {

    @Binds
    fun bindBookParser(mainBookParser: MainBookParser): BookParser
}
