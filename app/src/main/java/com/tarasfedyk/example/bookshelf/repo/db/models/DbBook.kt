package com.tarasfedyk.example.bookshelf.repo.db.models

data class DbBook (
    val info: DbBookInfo,
    val spineItems: List<DbSpineItem>
)
