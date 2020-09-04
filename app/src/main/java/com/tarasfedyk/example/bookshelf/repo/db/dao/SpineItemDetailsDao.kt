package com.tarasfedyk.example.bookshelf.repo.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.tarasfedyk.example.bookshelf.repo.db.models.DbSpineItemDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class SpineItemDetailsDao {
    fun getListFlow(bookId: String): Flow<List<DbSpineItemDetails>> =
        getListFlowAsIs(bookId).distinctUntilChanged()

    @Query("SELECT spine_items.*, book_metadatas.dirName FROM spine_items INNER JOIN book_metadatas ON spine_items.bookId = book_metadatas.id WHERE spine_items.bookId = :bookId ORDER BY spine_items.ordinal")
    protected abstract fun getListFlowAsIs(bookId: String): Flow<List<DbSpineItemDetails>>
}
