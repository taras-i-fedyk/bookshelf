package com.tarasfedyk.example.bookshelf.repo.dir

import android.content.Context
import java.io.File

const val BOOKS_DIR_PATH_WITHIN_ASSETS: String = "books"

fun BooksDir(appContext: Context): File = File(appContext.filesDir, "books")
