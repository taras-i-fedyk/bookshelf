package com.tarasfedyk.example.bookshelf.biz.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookInfo (
    val id: String,
    val title: String
) : Parcelable
