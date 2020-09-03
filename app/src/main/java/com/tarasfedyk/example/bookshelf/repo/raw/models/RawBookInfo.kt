package com.tarasfedyk.example.bookshelf.repo.raw.models

data class RawBookInfo (
    val metadata: RawBookMetadata,
    val spineFileIds: List<String>
)
