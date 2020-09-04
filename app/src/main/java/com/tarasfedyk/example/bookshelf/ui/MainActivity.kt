package com.tarasfedyk.example.bookshelf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.*
import com.tarasfedyk.example.bookshelf.R
import com.tarasfedyk.example.bookshelf.biz.*
import com.tarasfedyk.example.bookshelf.biz.di.qualifiers.BookMetadatasVmFactory
import com.tarasfedyk.example.bookshelf.biz.di.qualifiers.SpineFilePathsVmFactory
import com.tarasfedyk.example.bookshelf.repo.DbBooksMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject @BookMetadatasVmFactory lateinit var bookMetadatasVmFactory: ViewModelProvider.Factory
    private val bookMetadatasVm: BookMetadatasVm by viewModels { bookMetadatasVmFactory }

    @Inject @SpineFilePathsVmFactory lateinit var spineFilePathsVmFactory: ViewModelProvider.Factory
    private val spineFilePathsVm: SpineFilePathsVm by viewModels {spineFilePathsVmFactory}

    //@Inject lateinit var booksRepo: BooksRepo
    @Inject lateinit var dbBooksMediator: DbBooksMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            measureTimeMillis { dbBooksMediator.prepareDbBooks() }
                .also { println("XXX_YYY elapsed time = $it") }

            bookMetadatasVm.flow.first()

            val spineFilePaths = spineFilePathsVm.getFlow("code.google.com.epub-samples.moby-dick-basic").first()
            spineFilePaths.forEach {
                println("XXX_YYY spineFilePath = $it")
            }
        }
    }
}
