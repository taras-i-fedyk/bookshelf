package com.tarasfedyk.example.bookshelf.repo.db.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "spine_items",
    primaryKeys = ["bookId", "ordinal"],
    indices = [
        Index(value = ["bookId"]),
        Index(value = ["ordinal"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = DbBookInfo::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbSpineItem (
    val bookId: String,
    val ordinal: Int,
    val relativeFilePath: String
)
