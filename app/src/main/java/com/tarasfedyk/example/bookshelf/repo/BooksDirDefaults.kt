package com.tarasfedyk.example.bookshelf.repo

import android.content.Context
import java.io.File

const val BOOK_ASSETS_DIR_PATH: String = "books"

fun BooksDir(appContext: Context): File =
    File(appContext.getExternalFilesDir(null), "books")
    // File(appContext.filesDir, "books")
