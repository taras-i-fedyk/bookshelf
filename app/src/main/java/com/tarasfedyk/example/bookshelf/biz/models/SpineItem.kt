package com.tarasfedyk.example.bookshelf.biz.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
data class SpineItem (
    val relativeFilePath: String,
    val bookDir: File
) : Parcelable
