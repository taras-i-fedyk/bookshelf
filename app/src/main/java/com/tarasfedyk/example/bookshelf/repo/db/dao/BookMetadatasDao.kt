package com.tarasfedyk.example.bookshelf.repo.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookMetadata

@Dao
interface BookMetadatasDao {
    @Query("SELECT * FROM book_metadatas WHERE sourceFilePath = :sourceFilePath")
    suspend fun get(sourceFilePath: String): DbBookMetadata?

    @Query("SELECT * FROM book_metadatas ORDER BY ordinal")
    fun getPagingSource(): PagingSource<Int, DbBookMetadata>

    @Insert
    suspend fun add(bookMetadata: DbBookMetadata)
}
