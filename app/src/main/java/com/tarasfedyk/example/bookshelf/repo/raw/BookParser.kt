package com.tarasfedyk.example.bookshelf.repo.raw

import com.tarasfedyk.example.bookshelf.repo.raw.exceptions.BookFormatException
import com.tarasfedyk.example.bookshelf.repo.raw.models.RawBookDetails
import java.io.File
import kotlin.jvm.Throws

interface BookParser {
    @Throws(BookFormatException::class)
    suspend fun extractBookDetails(bookDir: File): RawBookDetails
}
