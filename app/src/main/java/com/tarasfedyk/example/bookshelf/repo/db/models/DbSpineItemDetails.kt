package com.tarasfedyk.example.bookshelf.repo.db.models

import androidx.room.Embedded

data class DbSpineItemDetails (
    @Embedded val spineItem: DbSpineItem,
    val dirName: String
)
