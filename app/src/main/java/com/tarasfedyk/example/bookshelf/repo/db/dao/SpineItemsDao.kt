package com.tarasfedyk.example.bookshelf.repo.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.tarasfedyk.example.bookshelf.repo.db.models.DbSpineItem

@Dao
interface SpineItemsDao {
    @Insert
    suspend fun addSpineItems(spineItems: List<DbSpineItem>)
}
