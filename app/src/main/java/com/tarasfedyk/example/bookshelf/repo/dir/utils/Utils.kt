package com.tarasfedyk.example.bookshelf.repo.dir.utils

import android.content.Context
import kotlinx.coroutines.yield
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun getFilePathsWithinAssets(appContext: Context, dirPathWithinAssets: String): List<String> =
    run {
        appContext.assets.list(dirPathWithinAssets)
            ?.map { filePathWithinAssets -> File(dirPathWithinAssets, filePathWithinAssets).path }
    } ?:
    emptyList()

suspend fun unzip(appContext: Context, sourceFilePathWithinAssets: String, destDir: File) {
    appContext.assets.open(sourceFilePathWithinAssets).use { inputStream ->
        ZipInputStream(inputStream).use { zipInputStream ->
            yield()

            var zipEntry: ZipEntry? = zipInputStream.nextEntry
            yield()

            while (zipEntry != null) {
                val destFile = File(destDir, zipEntry.name)
                destFile.parentFile!!.mkdirs()
                yield()

                FileOutputStream(destFile).use { outputStream ->
                    yield()

                    zipInputStream.copyTo(outputStream)
                    yield()
                }

                zipEntry = zipInputStream.nextEntry
                yield()
            }
        }
    }
}
