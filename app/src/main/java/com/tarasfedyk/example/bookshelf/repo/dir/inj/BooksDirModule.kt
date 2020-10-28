package com.tarasfedyk.example.bookshelf.repo.dir.inj

import android.content.Context
import com.tarasfedyk.example.bookshelf.repo.dir.inj.qualifiers.BooksDir
import com.tarasfedyk.example.bookshelf.repo.dir.BookParser
import com.tarasfedyk.example.bookshelf.repo.dir.BooksDir
import com.tarasfedyk.example.bookshelf.repo.dir.MainBookParser
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

@Module
@InstallIn(ApplicationComponent::class)
interface BooksDirModule {

    companion object {
        @Provides
        @BooksDir
        fun provideBooksDir(@ApplicationContext appContext: Context): File = BooksDir(appContext)
    }

    @Binds
    fun bindBookParser(mainBookParser: MainBookParser): BookParser
}
