package com.tarasfedyk.example.bookshelf.repo.dir

import com.tarasfedyk.example.bookshelf.repo.dir.exceptions.DirBookFormatException
import com.tarasfedyk.example.bookshelf.repo.dir.models.DirBook
import java.io.File
import kotlin.jvm.Throws

interface DirBookParser {
    @Throws(DirBookFormatException::class)
    suspend fun extractBook(bookDir: File): DirBook
}
