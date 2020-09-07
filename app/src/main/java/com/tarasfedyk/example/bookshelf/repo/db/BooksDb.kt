package com.tarasfedyk.example.bookshelf.repo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tarasfedyk.example.bookshelf.repo.db.dao.BookInfosDao
import com.tarasfedyk.example.bookshelf.repo.db.dao.ElaborateSpineItemsDao
import com.tarasfedyk.example.bookshelf.repo.db.dao.SpineItemsDao
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookInfo
import com.tarasfedyk.example.bookshelf.repo.db.models.DbSpineItem

@Database(
    entities = [
        DbBookInfo::class,
        DbSpineItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BooksDb : RoomDatabase() {
    abstract val bookInfosDao: BookInfosDao
    abstract val spineItemsDao: SpineItemsDao
    abstract val elaborateSpineItemsDao: ElaborateSpineItemsDao
}
