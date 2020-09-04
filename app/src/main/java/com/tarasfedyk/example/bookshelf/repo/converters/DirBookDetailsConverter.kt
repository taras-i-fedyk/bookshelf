package com.tarasfedyk.example.bookshelf.repo.converters

import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookDetails
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookMetadata
import com.tarasfedyk.example.bookshelf.repo.db.models.DbSpineItem
import com.tarasfedyk.example.bookshelf.repo.dir.models.DirBookDetails
import javax.inject.Inject

class DirBookDetailsConverter @Inject constructor() {

    fun toDbBookDetails(
        dirBookDetails: DirBookDetails,
        dbBookOrdinal: Int,
        dbBookDirName: String,
        dbBookSourceFilePath: String
    ): DbBookDetails {
        val dbBookMetadata =
            DbBookMetadata(
                id = dirBookDetails.metadata.id,
                ordinal = dbBookOrdinal,
                title = dirBookDetails.metadata.title,
                dirName = dbBookDirName,
                sourceFilePath = dbBookSourceFilePath
            )
        val dbSpineItems = mutableListOf<DbSpineItem>()
        dirBookDetails.relativePathsOfSpineFiles.forEachIndexed { index, relativePathOfSpineFile ->
            val dbSpineItem =
                DbSpineItem(
                    bookId = dirBookDetails.metadata.id,
                    ordinal = index + 1,
                    relativeFilePath = relativePathOfSpineFile
                )
            dbSpineItems.add(dbSpineItem)
        }
        return DbBookDetails(dbBookMetadata, dbSpineItems)
    }
}
