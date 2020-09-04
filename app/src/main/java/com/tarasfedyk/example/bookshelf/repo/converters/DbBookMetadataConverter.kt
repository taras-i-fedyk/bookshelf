package com.tarasfedyk.example.bookshelf.repo.converters

import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookMetadata
import javax.inject.Inject

class DbBookMetadataConverter @Inject constructor() {

    fun toBookMetadata(dbBookMetadata: DbBookMetadata): BookMetadata =
        BookMetadata(
            id = dbBookMetadata.id,
            title = dbBookMetadata.title
        )
}
