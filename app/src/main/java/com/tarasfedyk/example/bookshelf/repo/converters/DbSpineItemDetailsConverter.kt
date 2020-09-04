package com.tarasfedyk.example.bookshelf.repo.converters

import com.tarasfedyk.example.bookshelf.repo.db.models.DbSpineItemDetails
import com.tarasfedyk.example.bookshelf.repo.di.qualifiers.BooksDir
import java.io.File
import javax.inject.Inject

class DbSpineItemDetailsConverter @Inject constructor(
    @BooksDir private val booksDir: File
) {

    fun toSpineFile(dbSpineItemDetails: DbSpineItemDetails): File =
        File(
            File(booksDir, dbSpineItemDetails.dirName),
            dbSpineItemDetails.spineItem.relativeFilePath
        )
}
