package com.tarasfedyk.example.bookshelf.ui.di

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import com.tarasfedyk.example.bookshelf.ui.BookMetadatasAdapter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface BooksFragmentModule {

    @Binds
    fun bindBookMetadatasAdapter(
        bookMetadatasAdapter: BookMetadatasAdapter
    ): PagingDataAdapter<BookMetadata, out RecyclerView.ViewHolder>
}
