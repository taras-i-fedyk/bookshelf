package com.tarasfedyk.example.bookshelf.biz.di

import androidx.lifecycle.ViewModelProvider
import com.tarasfedyk.example.bookshelf.biz.MainSpineFilePathsVmFactory
import com.tarasfedyk.example.bookshelf.biz.di.qualifiers.SpineFilePathsVmFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface SpineFilePathsVmModule {

    @Binds
    @SpineFilePathsVmFactory
    fun bindSpineFilePathsVmFactory(
        mainSpineFilePathsVmFactory: MainSpineFilePathsVmFactory
    ): ViewModelProvider.Factory
}
