package com.tarasfedyk.example.bookshelf.repo.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookInfo

@Dao
interface BookInfosDao {
    @Query("SELECT * FROM book_infos WHERE sourceFilePath = :sourceFilePath")
    suspend fun getBookInfo(sourceFilePath: String): DbBookInfo?

    @Query("SELECT * FROM book_infos ORDER BY ordinal")
    fun getPagingSource(): PagingSource<Int, DbBookInfo>

    @Insert
    suspend fun addBookInfos(bookInfo: DbBookInfo)
}
