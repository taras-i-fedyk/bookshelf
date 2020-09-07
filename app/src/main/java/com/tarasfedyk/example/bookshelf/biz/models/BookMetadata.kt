package com.tarasfedyk.example.bookshelf.biz.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookMetadata (
    val id: String,
    val title: String
) : Parcelable
