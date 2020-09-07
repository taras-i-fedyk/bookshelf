package com.tarasfedyk.example.bookshelf.biz.models

data class RetrievalResult<T> (
    val retrievedItems: List<T>,
    val areMoreItemsAvailable: Boolean
)
