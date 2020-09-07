package com.tarasfedyk.example.bookshelf.ui.adapters.di

import com.tarasfedyk.example.bookshelf.ui.adapters.MainSpineItemsAdapter
import com.tarasfedyk.example.bookshelf.ui.adapters.SpineItemsAdapter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface SpineItemsAdapterModule {

    @Binds
    fun bindSpineItemsAdapter(mainSpineItemsAdapter: MainSpineItemsAdapter): SpineItemsAdapter
}
