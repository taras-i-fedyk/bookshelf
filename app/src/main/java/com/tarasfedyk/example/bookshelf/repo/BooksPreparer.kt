package com.tarasfedyk.example.bookshelf.repo

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.*
import com.tarasfedyk.example.bookshelf.repo.utils.getAssetFilePaths
import com.tarasfedyk.example.bookshelf.repo.utils.unzip
import com.tarasfedyk.example.bookshelf.repo.di.qualifiers.BooksDir
import com.tarasfedyk.example.bookshelf.repo.raw.BookParser
import com.tarasfedyk.example.bookshelf.repo.raw.exceptions.BookFormatException
import kotlinx.coroutines.*
import java.io.File
import kotlin.jvm.Throws

class BooksPreparer @WorkerInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    @BooksDir private val booksDir: File,
    private val bookParser: BookParser
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {
        val bookAssetFilePaths = getAssetFilePaths(appContext, BOOK_ASSETS_DIR_PATH)
        val jobs =
            bookAssetFilePaths.map { bookAssetFilePath ->
                async { prepareBook(bookAssetFilePath) }
            }
        jobs.awaitAll()
        Result.success()
    }

    @Throws(BookFormatException::class)
    private suspend fun prepareBook(bookAssetFilePath: String) {
        // TODO: do the work only if there's no corresponding data in the DB

        val bookAssetFileName = File(bookAssetFilePath).name
        val bookDir = File(booksDir, bookAssetFileName)
        unzip(appContext, sourceAssetFilePath = bookAssetFilePath, destDir = bookDir)

        val rawBookDetails = bookParser.extractBookDetails(bookDir)
        println("XXX_YYY rawBookDetails = $rawBookDetails")

        // TODO: add the data to the DB
    }
}
