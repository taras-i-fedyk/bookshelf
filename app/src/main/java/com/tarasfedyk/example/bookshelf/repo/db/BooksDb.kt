package com.tarasfedyk.example.bookshelf.repo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tarasfedyk.example.bookshelf.repo.db.dao.BookMetadatasDao
import com.tarasfedyk.example.bookshelf.repo.db.dao.SpineItemDetailsDao
import com.tarasfedyk.example.bookshelf.repo.db.dao.SpineItemsDao
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookMetadata
import com.tarasfedyk.example.bookshelf.repo.db.models.DbSpineItem
import com.tarasfedyk.example.bookshelf.repo.db.models.DbSpineItemDetails

@Database(
    entities = [
        DbBookMetadata::class,
        DbSpineItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BooksDb : RoomDatabase() {
    abstract val bookMetadatasDao: BookMetadatasDao
    abstract val spineItemsDao: SpineItemsDao
    abstract val spineItemDetailsDao: SpineItemDetailsDao
}
