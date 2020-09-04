package com.tarasfedyk.example.bookshelf.repo.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "book_metadatas",
    indices = [
        Index(value = ["ordinal"], unique = true),
        Index(value = ["dirName"], unique = true),
        Index(value = ["sourceFilePath"], unique = true)
    ]
)
data class DbBookMetadata (
    @PrimaryKey val id: String,
    val ordinal: Int,
    val title: String,
    val dirName: String,
    val sourceFilePath: String
)
