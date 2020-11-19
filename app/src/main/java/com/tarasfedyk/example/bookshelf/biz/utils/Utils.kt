package com.tarasfedyk.example.bookshelf.biz.utils

import android.os.Looper

fun isMainThread(): Boolean = Thread.currentThread() == Looper.getMainLooper().thread

fun enforceMainThread() = check(isMainThread()) { "The current thread is not the main thread" }
