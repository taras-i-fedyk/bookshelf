package com.tarasfedyk.example.bookshelf.repo.converters

import com.tarasfedyk.example.bookshelf.biz.models.SpineItem
import com.tarasfedyk.example.bookshelf.repo.db.models.DbSpineItemDetails
import com.tarasfedyk.example.bookshelf.repo.di.qualifiers.BooksDir
import java.io.File
import javax.inject.Inject

class DbSpineItemDetailsConverter @Inject constructor(
    @BooksDir private val booksDir: File
) {

    fun toSpineItem(dbSpineItemDetails: DbSpineItemDetails): SpineItem =
        SpineItem(
            relativeFilePath = dbSpineItemDetails.spineItem.relativeFilePath,
            bookDir = File(booksDir, dbSpineItemDetails.bookDirName)
        )
}
