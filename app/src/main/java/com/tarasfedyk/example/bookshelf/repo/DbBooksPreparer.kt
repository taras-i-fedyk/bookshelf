package com.tarasfedyk.example.bookshelf.repo

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.room.withTransaction
import androidx.work.*
import com.tarasfedyk.example.bookshelf.repo.converters.DirBookConverter
import com.tarasfedyk.example.bookshelf.repo.db.BooksDb
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBook
import com.tarasfedyk.example.bookshelf.repo.dir.utils.getFilePathsWithinAssets
import com.tarasfedyk.example.bookshelf.repo.dir.utils.unzip
import com.tarasfedyk.example.bookshelf.repo.inj.qualifiers.BooksDir
import com.tarasfedyk.example.bookshelf.repo.dir.BOOKS_DIR_PATH_WITHIN_ASSETS
import com.tarasfedyk.example.bookshelf.repo.dir.BookParser
import com.tarasfedyk.example.bookshelf.repo.dir.exceptions.DirBookFormatException
import java.io.File
import kotlin.jvm.Throws

class DbBooksPreparer @WorkerInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    @BooksDir private val booksDir: File,
    private val bookParser: BookParser,
    private val booksDb: BooksDb,
    private val dirBookConverter: DirBookConverter
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {
        val bookFilePathsWithinAssets =
            getFilePathsWithinAssets(appContext, BOOKS_DIR_PATH_WITHIN_ASSETS)
        val pendingDbBooks =
            bookFilePathsWithinAssets
                .mapIndexed { index, bookFilePathWithinAssets ->
                    getPendingDbBook(dbBookOrdinal = index + 1, bookFilePathWithinAssets)
                }
                .filterNotNull()
        booksDb.withTransaction {
            for (pendingDbBook in pendingDbBooks) {
                booksDb.bookInfosDao.addBookInfos(pendingDbBook.info)
                booksDb.spineItemsDao.addSpineItems(pendingDbBook.spineItems)
            }
        }
        return Result.success()
    }

    @Throws(DirBookFormatException::class)
    private suspend fun getPendingDbBook(
        dbBookOrdinal: Int,
        bookFilePathWithinAssets: String,
    ): DbBook? {
        if (isDbBookPrepared(bookFilePathWithinAssets)) return null

        val bookFileNameWithinAssets = File(bookFilePathWithinAssets).name
        val bookDir = File(booksDir, bookFileNameWithinAssets)
        unzip(appContext, sourceFilePathWithinAssets = bookFilePathWithinAssets, destDir = bookDir)

        val dirBook = bookParser.extractBook(bookDir)

        return dirBookConverter.toDbBook(
            dirBook,
            dbBookOrdinal = dbBookOrdinal,
            dbBookDirName = bookFileNameWithinAssets,
            dbBookSourceFilePath = bookFilePathWithinAssets
        )
    }

    private suspend fun isDbBookPrepared(bookFilePathWithinAssets: String) =
        booksDb.bookInfosDao.getBookInfo(bookFilePathWithinAssets) != null
}
