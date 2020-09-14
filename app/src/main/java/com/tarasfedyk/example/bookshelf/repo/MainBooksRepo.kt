package com.tarasfedyk.example.bookshelf.repo

import androidx.paging.*
import com.tarasfedyk.example.bookshelf.biz.BooksRepo
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import com.tarasfedyk.example.bookshelf.biz.models.SpineItem
import com.tarasfedyk.example.bookshelf.repo.converters.DbBookInfoConverter
import com.tarasfedyk.example.bookshelf.repo.converters.DbElaborateSpineItemConverter
import com.tarasfedyk.example.bookshelf.repo.db.BooksDb
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class MainBooksRepo @Inject constructor(
    private val dbBooksMediatorProvider: Provider<RemoteMediator<Int, DbBookInfo>>,
    private val booksDb: BooksDb,
    private val dbBookInfoConverter: DbBookInfoConverter,
    private val dbElaborateSpineItemConverter: DbElaborateSpineItemConverter
) : BooksRepo {

    override fun createBookInfosFlow(): Flow<PagingData<BookInfo>> {
        val dbPager = Pager(
            config = PagingConfig(
                pageSize = BooksRepoConstants.DB_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = dbBooksMediatorProvider.get(),
            pagingSourceFactory = { booksDb.bookInfosDao.getPagingSource() }
        )
        return dbPager.flow.map { dbPagingData ->
            dbPagingData.map { dbBookInfo ->
                dbBookInfoConverter.toBookInfo(dbBookInfo)
            }
        }
    }

    override fun createSpineItemsFlow(bookId: String): Flow<List<SpineItem>> =
        booksDb.elaborateSpineItemsDao.getListFlow(bookId).map { dbSpineItems ->
            dbSpineItems.map { dbSpineItem ->
                dbElaborateSpineItemConverter.toSpineItem(dbSpineItem)
            }
        }
}
