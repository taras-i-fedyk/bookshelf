package com.tarasfedyk.example.bookshelf.repo.db.models

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class DbElaborateSpineItem (
    @ColumnInfo(name = "dirName") val bookDirName: String,
    @Embedded val spineItem: DbSpineItem
)
