package com.tarasfedyk.example.bookshelf.repo.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.tarasfedyk.example.bookshelf.repo.db.models.DbElaborateSpineItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class ElaborateSpineItemsDao {
    fun getListFlow(bookId: String): Flow<List<DbElaborateSpineItem>> =
        getListFlowAsIs(bookId).distinctUntilChanged()

    @Query("SELECT book_infos.dirName, spine_items.* FROM spine_items INNER JOIN book_infos ON spine_items.bookId = book_infos.id WHERE spine_items.bookId = :bookId ORDER BY spine_items.ordinal")
    protected abstract fun getListFlowAsIs(bookId: String): Flow<List<DbElaborateSpineItem>>
}
