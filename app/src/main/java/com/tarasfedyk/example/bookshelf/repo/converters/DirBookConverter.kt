package com.tarasfedyk.example.bookshelf.repo.converters

import com.tarasfedyk.example.bookshelf.repo.db.models.DbBook
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookInfo
import com.tarasfedyk.example.bookshelf.repo.db.models.DbSpineItem
import com.tarasfedyk.example.bookshelf.repo.dir.models.DirBook
import javax.inject.Inject

class DirBookConverter @Inject constructor() {

    fun toDbBook(
        dirBook: DirBook,
        dbBookOrdinal: Int,
        dbBookDirName: String,
        dbBookSourceFilePath: String
    ): DbBook {
        val dbBookInfo =
            DbBookInfo(
                id = dirBook.info.id,
                ordinal = dbBookOrdinal,
                title = dirBook.info.title,
                dirName = dbBookDirName,
                sourceFilePath = dbBookSourceFilePath
            )
        val dbSpineItems = mutableListOf<DbSpineItem>()
        dirBook.relativePathsOfSpineItemFiles
            .forEachIndexed { index, relativePathOfSpineItemFile ->
                val dbSpineItem =
                    DbSpineItem(
                        bookId = dirBook.info.id,
                        ordinal = index + 1,
                        relativeFilePath = relativePathOfSpineItemFile
                    )
                dbSpineItems.add(dbSpineItem)
            }
        return DbBook(dbBookInfo, dbSpineItems)
    }
}
