package com.tarasfedyk.example.bookshelf.repo.db

import android.content.Context
import androidx.room.Room

private const val DB_FILE_NAME: String = "Books.db"

fun BooksDb(appContext: Context): BooksDb =
    Room
        .databaseBuilder(appContext, BooksDb::class.java, DB_FILE_NAME)
        .fallbackToDestructiveMigration()
        .build()
