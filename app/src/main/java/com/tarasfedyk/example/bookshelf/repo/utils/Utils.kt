package com.tarasfedyk.example.bookshelf.repo.utils

import android.content.Context
import kotlinx.coroutines.yield
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun getAssetFilePaths(appContext: Context, assetsDirPath: String): List<String> =
    run {
        appContext.assets.list(assetsDirPath)
            ?.map { assetFilePath -> File(assetsDirPath, assetFilePath).path }
    } ?:
    emptyList()

suspend fun unzip(appContext: Context, sourceAssetFilePath: String, destDir: File) {
    appContext.assets.open(sourceAssetFilePath).use { inputStream ->
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
