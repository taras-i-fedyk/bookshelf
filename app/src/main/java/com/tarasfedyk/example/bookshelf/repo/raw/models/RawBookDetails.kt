package com.tarasfedyk.example.bookshelf.repo.raw.models

data class RawBookDetails (
    val metadata: RawBookMetadata,
    val relativeSpineFilePaths: List<String>
)
