package com.tarasfedyk.example.bookshelf.repo

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.room.withTransaction
import androidx.work.*
import com.tarasfedyk.example.bookshelf.repo.converters.DirBookDetailsConverter
import com.tarasfedyk.example.bookshelf.repo.db.BooksDb
import com.tarasfedyk.example.bookshelf.repo.dir.utils.getFilePathsWithinAssets
import com.tarasfedyk.example.bookshelf.repo.dir.utils.unzip
import com.tarasfedyk.example.bookshelf.repo.di.qualifiers.BooksDir
import com.tarasfedyk.example.bookshelf.repo.dir.BOOKS_DIR_PATH_WITHIN_ASSETS
import com.tarasfedyk.example.bookshelf.repo.dir.BookParser
import com.tarasfedyk.example.bookshelf.repo.dir.exceptions.BookFormatException
import kotlinx.coroutines.*
import java.io.File
import kotlin.jvm.Throws

class DbBooksPreparer @WorkerInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    @BooksDir private val booksDir: File,
    private val bookParser: BookParser,
    private val booksDb: BooksDb,
    private val dirBookDetailsConverter: DirBookDetailsConverter
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {
        val bookFilePathsWithinAssets = getFilePathsWithinAssets(appContext, BOOKS_DIR_PATH_WITHIN_ASSETS)
        val jobs =
            bookFilePathsWithinAssets.mapIndexed() { index, bookFilePathWithinAssets ->
                async {
                    prepareDbBook(dbBookOrdinal = index + 1, bookFilePathWithinAssets)
                }
            }
        jobs.awaitAll()
        Result.success()
    }

    @Throws(BookFormatException::class)
    private suspend fun prepareDbBook(dbBookOrdinal: Int, bookFilePathWithinAssets: String) {
        if (booksDb.bookMetadatasDao.get(bookFilePathWithinAssets) != null) {
            return
        }

        val bookFileNameWithinAssets = File(bookFilePathWithinAssets).name
        val bookDir = File(booksDir, bookFileNameWithinAssets)
        unzip(appContext, sourceFilePathWithinAssets = bookFilePathWithinAssets, destDir = bookDir)

        val dirBookDetails = bookParser.extractBookDetails(bookDir)

        val (dbBookMetadata, dbSpineItems) =
            dirBookDetailsConverter.toDbModels(
                dirBookDetails,
                dbBookOrdinal = dbBookOrdinal,
                dbBookDirName = bookFileNameWithinAssets,
                dbBookSourceFilePath = bookFilePathWithinAssets
            )
        booksDb.withTransaction {
            booksDb.bookMetadatasDao.add(dbBookMetadata)
            booksDb.spineItemsDao.add(dbSpineItems)
        }
    }
}
