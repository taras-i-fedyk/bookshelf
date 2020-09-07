package com.tarasfedyk.example.bookshelf.biz.inj

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tarasfedyk.example.bookshelf.biz.MainSpineItemsVmFactory
import com.tarasfedyk.example.bookshelf.biz.SpineItemsVm
import com.tarasfedyk.example.bookshelf.biz.inj.qualifiers.SpineItemsVmFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface SpineItemsVmModule {

    companion object {
        @Provides
        fun provideSpineItemsVm(
            fragment: Fragment,
            @SpineItemsVmFactory spineItemsVmFactory: ViewModelProvider.Factory
        ): SpineItemsVm {
            val lazySpineItemsVm = fragment.viewModels<SpineItemsVm> { spineItemsVmFactory }
            return lazySpineItemsVm.value
        }
    }

    @Binds
    @SpineItemsVmFactory
    fun bindSpineItemsVmFactory(
        mainSpineItemsVmFactory: MainSpineItemsVmFactory
    ): ViewModelProvider.Factory
}
