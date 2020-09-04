package com.tarasfedyk.example.bookshelf.repo.dir

import com.tarasfedyk.example.bookshelf.repo.dir.exceptions.BookFormatException
import com.tarasfedyk.example.bookshelf.repo.dir.models.DirBookDetails
import java.io.File
import kotlin.jvm.Throws

interface BookParser {
    @Throws(BookFormatException::class)
    suspend fun extractBookDetails(bookDir: File): DirBookDetails
}
