package com.tarasfedyk.example.bookshelf.repo.db.models

data class DbBookDetails (
    val metadata: DbBookMetadata,
    val spineItems: List<DbSpineItem>
)
