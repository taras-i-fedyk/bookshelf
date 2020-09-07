package com.tarasfedyk.example.bookshelf.repo.converters

import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookInfo
import javax.inject.Inject

class DbBookInfoConverter @Inject constructor() {

    fun toBookInfo(dbBookInfo: DbBookInfo): BookInfo =
        BookInfo(
            id = dbBookInfo.id,
            title = dbBookInfo.title
        )
}
