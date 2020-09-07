package com.tarasfedyk.example.bookshelf.repo.converters

import com.tarasfedyk.example.bookshelf.biz.models.SpineItem
import com.tarasfedyk.example.bookshelf.repo.db.models.DbElaborateSpineItem
import com.tarasfedyk.example.bookshelf.repo.inj.qualifiers.BooksDir
import java.io.File
import javax.inject.Inject

class DbElaborateSpineItemConverter @Inject constructor(
    @BooksDir private val booksDir: File
) {

    fun toSpineItem(dbElaborateSpineItem: DbElaborateSpineItem): SpineItem =
        SpineItem(
            relativeFilePath = dbElaborateSpineItem.spineItem.relativeFilePath,
            bookDir = File(booksDir, dbElaborateSpineItem.bookDirName)
        )
}
