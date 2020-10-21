package com.tarasfedyk.example.bookshelf.repo.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookInfo

@Dao
interface BookInfosDao {
    @Query("SELECT COUNT(*) FROM book_infos WHERE ordinal BETWEEN :firstOrdinal AND :lastOrdinal")
    suspend fun getBookInfoCount(firstOrdinal: Int, lastOrdinal: Int): Int

    @Query("SELECT * FROM book_infos ORDER BY ordinal")
    fun getPagingSource(): PagingSource<Int, DbBookInfo>

    @Insert
    suspend fun addBookInfo(bookInfo: DbBookInfo)
}
