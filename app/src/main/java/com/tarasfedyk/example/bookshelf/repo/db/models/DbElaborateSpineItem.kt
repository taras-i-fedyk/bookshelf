package com.tarasfedyk.example.bookshelf.repo.db.models

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class DbElaborateSpineItem (
    @Embedded val spineItem: DbSpineItem,
    @ColumnInfo(name = "dirName") val bookDirName: String
)
