package com.tarasfedyk.example.bookshelf.repo.dir.utils

import android.content.Context
import com.tarasfedyk.example.bookshelf.biz.models.RetrievalResult
import kotlinx.coroutines.yield
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun getFilePathsWithinAssets(
    appContext: Context,
    parentDirPath: String,
    firstFileOrdinal: Int,
    lastFileOrdinal: Int
): RetrievalResult<String> {
    val allFilePaths = appContext.assets.list(parentDirPath)
    if (allFilePaths == null) {
        return RetrievalResult(
            retrievedItems = emptyList(),
            areMoreItemsAvailable = false
        )
    }

    val filteredFilePaths = allFilePaths
        .filterIndexed { index, _ ->
            val ordinal = index + 1
            ordinal in firstFileOrdinal..lastFileOrdinal
        }
        .map { filteredFilePath ->
            File(parentDirPath, filteredFilePath).path
        }
    return RetrievalResult(
        retrievedItems = filteredFilePaths,
        areMoreItemsAvailable = lastFileOrdinal >= allFilePaths.size
    )
}

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
