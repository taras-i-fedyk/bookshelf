package com.tarasfedyk.example.bookshelf.biz.di

import androidx.lifecycle.ViewModelProvider
import com.tarasfedyk.example.bookshelf.biz.MainBookMetadatasVmFactory
import com.tarasfedyk.example.bookshelf.biz.di.qualifiers.BookMetadatasVmFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface BookMetadatasVmModule {

    @Binds
    @BookMetadatasVmFactory
    fun bindBookMetadatasVmFactory(
        mainBookMetadatasVmFactory: MainBookMetadatasVmFactory
    ): ViewModelProvider.Factory
}
