package com.tarasfedyk.example.bookshelf.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tarasfedyk.example.bookshelf.biz.BooksRepo
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import com.tarasfedyk.example.bookshelf.repo.converters.DbBookMetadataConverter
import com.tarasfedyk.example.bookshelf.repo.converters.DbSpineItemDetailsConverter
import com.tarasfedyk.example.bookshelf.repo.db.BooksDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class MainBooksRepo @Inject constructor(
    private val dbBooksMediatorProvider: Provider<DbBooksMediator>,
    private val booksDb: BooksDb,
    private val dbBookMetadataConverter: DbBookMetadataConverter,
    private val dbSpineItemDetailsConverter: DbSpineItemDetailsConverter
) : BooksRepo {

    override fun createBookMetadatasFlow(): Flow<PagingData<BookMetadata>> {
        val dbPager = Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = dbBooksMediatorProvider.get(),
            pagingSourceFactory = { booksDb.bookMetadatasDao.getPagingSource() }
        )
        return dbPager.flow.map { dbPagingData ->
            dbPagingData.map { dbBookMetadata ->
                dbBookMetadataConverter.toBookMetadata(dbBookMetadata)
            }
        }
    }

    override fun createSpineFilePathsFlow(bookId: String): Flow<List<String>> =
        booksDb.spineItemDetailsDao.getListFlow(bookId).map { listOfDbSpineItemDetails ->
            listOfDbSpineItemDetails.map { dbSpineItemDetails ->
                dbSpineItemDetailsConverter.toSpineFile(dbSpineItemDetails).path
            }
        }

    private companion object {
        private const val PAGE_SIZE: Int = 10
    }
}
