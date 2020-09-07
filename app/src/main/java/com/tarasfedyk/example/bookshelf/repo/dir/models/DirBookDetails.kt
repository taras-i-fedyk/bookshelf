package com.tarasfedyk.example.bookshelf.repo.dir.models

data class DirBookDetails (
    val metadata: DirBookMetadata,
    val relativePathsOfSpineItemFiles: List<String>
)
