package com.tarasfedyk.example.bookshelf.ui.adapters.di

import androidx.recyclerview.widget.DiffUtil
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import com.tarasfedyk.example.bookshelf.ui.adapters.*
import com.tarasfedyk.example.bookshelf.ui.adapters.di.qualifiers.BookInfosDiffCallback
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface BookInfosAdapterModule {

    @Binds
    fun bindBookInfosAdapterFactory(
        mainBookInfosAdapterFactory: MainBookInfosAdapterFactory
    ): BookInfosAdapterFactory

    @Binds
    @BookInfosDiffCallback
    fun bindBookInfosDiffCallback(
        mainBookInfosDiffCallback: MainBookInfosDiffCallback
    ): DiffUtil.ItemCallback<BookMetadata>
}
