package com.tarasfedyk.example.bookshelf.ui.adapters.inj

import androidx.recyclerview.widget.DiffUtil
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import com.tarasfedyk.example.bookshelf.ui.adapters.*
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
    fun bindBookInfosDiffCallback(
        mainBookInfosDiffCallback: MainBookInfosDiffCallback
    ): DiffUtil.ItemCallback<BookInfo>

    @Binds
    fun bindLoadStateAdapterFactory(
        mainLoadStateAdapterFactory: MainLoadStateAdapterFactory
    ): LoadStateAdapterFactory
}
