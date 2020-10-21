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

class MainDbBooksFetcher @WorkerInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    @BooksDir private val booksDir: File,
    private val bookParser: BookParser,
    private val booksDb: BooksDb,
    private val dirBookConverter: DirBookConverter
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {
        val firstDbBookOrdinal = inputData.getInt(BooksRepoKeys.FIRST_DB_BOOK_ORDINAL, 0)
        val lastDbBookOrdinal = inputData.getInt(BooksRepoKeys.LAST_DB_BOOK_ORDINAL, 0)
        if (booksDb.bookInfosDao.getBookInfoCount(firstDbBookOrdinal, lastDbBookOrdinal) > 0) {
            return Result.success(
                workDataOf(
                    BooksRepoKeys.ARE_MORE_DB_BOOKS_AVAILABLE to true)
            )
        }

        val retrievalResult =
            getFilePathsWithinAssets(
                appContext,
                BOOKS_DIR_PATH_WITHIN_ASSETS,
                firstDbBookOrdinal,
                lastDbBookOrdinal)
        val bookFilePathsWithinAssets = retrievalResult.retrievedItems
        val areMoreBookFilePathsWithinAssetsAvailable = retrievalResult.areMoreItemsAvailable

        val dbBooks =
            bookFilePathsWithinAssets.mapIndexed { index, bookFilePathWithinAssets ->
                prepareDbBook(
                    dbBookOrdinal = firstDbBookOrdinal + index,
                    bookFilePathWithinAssets
                )
            }
        booksDb.withTransaction {
            for (dbBook in dbBooks) {
                booksDb.bookInfosDao.addBookInfo(dbBook.info)
                booksDb.spineItemsDao.addSpineItems(dbBook.spineItems)
            }
        }
        return Result.success(
            workDataOf(
                Pair (
                    BooksRepoKeys.ARE_MORE_DB_BOOKS_AVAILABLE,
                    areMoreBookFilePathsWithinAssetsAvailable
                )
            )
        )
    }

    @Throws(DirBookFormatException::class)
    private suspend fun prepareDbBook(
        dbBookOrdinal: Int,
        bookFilePathWithinAssets: String,
    ): DbBook {
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
}
