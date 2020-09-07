package com.tarasfedyk.example.bookshelf.repo.dir.models

data class DirBook (
    val info: DirBookInfo,
    val relativePathsOfSpineItemFiles: List<String>
)
