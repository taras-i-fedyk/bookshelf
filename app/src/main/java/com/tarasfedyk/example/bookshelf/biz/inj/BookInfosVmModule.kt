package com.tarasfedyk.example.bookshelf.biz.inj

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tarasfedyk.example.bookshelf.biz.BookInfosVm
import com.tarasfedyk.example.bookshelf.biz.MainBookInfosVmFactory
import com.tarasfedyk.example.bookshelf.biz.inj.qualifiers.BookInfosVmFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface BookInfosVmModule {

    companion object {
        @Provides
        fun provideBookInfosVm(
            fragment: Fragment,
            @BookInfosVmFactory bookInfosVmFactory: ViewModelProvider.Factory
        ): BookInfosVm {
            val lazyBookInfosVm = fragment.viewModels<BookInfosVm> { bookInfosVmFactory }
            return lazyBookInfosVm.value
        }
    }

    @Binds
    @BookInfosVmFactory
    fun bindBookInfosVmFactory(
        mainBookInfosVmFactory: MainBookInfosVmFactory
    ): ViewModelProvider.Factory
}
