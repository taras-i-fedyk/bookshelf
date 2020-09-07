package com.tarasfedyk.example.bookshelf.repo.dir

import android.util.Xml
import com.tarasfedyk.example.bookshelf.repo.dir.exceptions.DirBookFormatException
import com.tarasfedyk.example.bookshelf.repo.dir.models.DirBook
import com.tarasfedyk.example.bookshelf.repo.dir.models.DirRawBook
import com.tarasfedyk.example.bookshelf.repo.dir.models.DirBookInfo
import kotlinx.coroutines.yield
import org.xmlpull.v1.XmlPullParser
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject
import kotlin.jvm.Throws

class MainDirBookParser @Inject constructor() : DirBookParser {

    @Throws(DirBookFormatException::class)
    override suspend fun extractBook(bookDir: File): DirBook {
        val relativePathOfContentFile = extractRelativePathOfContentFile(bookDir)
        val rawBook = extractRawBook(bookDir, relativePathOfContentFile)
        val relativePathsOfSpineItemFiles =
            extractRelativePathsOfSpineItemFiles(
                bookDir, relativePathOfContentFile, rawBook.spineItemIds)
        return DirBook(
            info = rawBook.info,
            relativePathsOfSpineItemFiles = relativePathsOfSpineItemFiles
        )
    }

    @Throws(DirBookFormatException::class)
    private suspend fun extractRelativePathOfContentFile(bookDir: File): String {
        var relativePathOfContentFile: String? = null

        val containerFile = File(bookDir, "META-INF/container.xml")
        FileInputStream(containerFile).use { inputStream ->
            yield()

            val xmlParser: XmlPullParser = Xml.newPullParser()
            xmlParser.setInput(inputStream, null)
            yield()

            while (xmlParser.next() != XmlPullParser.END_DOCUMENT) {
                yield()

                if (xmlParser.eventType == XmlPullParser.START_TAG) {
                    if (xmlParser.name == TAGS.ROOTFILE) {
                        val relativeFilePath =
                            xmlParser.getAttributeValue(null, "full-path")
                        if (relativeFilePath.endsWith("opf", true)) {
                            relativePathOfContentFile = relativeFilePath
                            break
                        }
                    }
                }
            }
        }
        yield()

        return relativePathOfContentFile ?: throw DirBookFormatException()
    }

    @Throws(DirBookFormatException::class)
    private suspend fun extractRawBook(
        bookDir: File,
        relativePathOfContentFile: String
    ): DirRawBook {
        var bookInfo: DirBookInfo? = null
        var spineItemIds: List<String>? = null

        val contentFile = File(bookDir, relativePathOfContentFile)
        FileInputStream(contentFile).use { inputStream ->
            yield()

            val xmlParser: XmlPullParser = Xml.newPullParser()
            xmlParser.setInput(inputStream, null)
            yield()

            while (xmlParser.next() != XmlPullParser.END_DOCUMENT) {
                yield()

                if (xmlParser.eventType == XmlPullParser.START_TAG) {
                    when (xmlParser.name) {
                        TAGS.METADATA -> bookInfo = readBookInfo(xmlParser)
                        TAGS.SPINE -> {
                            spineItemIds = readSpineItemIds(xmlParser)
                            break
                        }
                    }
                }
            }
        }
        yield()

        return DirRawBook(
            info = bookInfo ?: throw DirBookFormatException(),
            spineItemIds = spineItemIds ?: throw DirBookFormatException()
        )
    }

    @Throws(DirBookFormatException::class)
    private suspend fun readBookInfo(xmlParser: XmlPullParser): DirBookInfo {
        var id: String? = null
        var title: String? = null

        xmlParser.require(XmlPullParser.START_TAG, null, TAGS.METADATA)
        yield()

        while (!(xmlParser.next() == XmlPullParser.END_TAG && xmlParser.name == TAGS.METADATA)) {
            yield()

            if (xmlParser.eventType == XmlPullParser.START_TAG) {
                when (xmlParser.name) {
                    TAGS.IDENTIFIER -> id = readText(xmlParser)
                    TAGS.TITLE -> title = readText(xmlParser)
                }
            }
        }
        yield()

        return DirBookInfo(
            id = id ?: throw DirBookFormatException(),
            title = title ?: throw DirBookFormatException()
        )
    }

    @Throws(DirBookFormatException::class)
    private suspend fun readText(xmlParser: XmlPullParser): String {
        var text: String? = null

        if (xmlParser.next() == XmlPullParser.TEXT) {
            yield()

            text = xmlParser.text
            xmlParser.next()
            yield()
        }

        return text ?: throw DirBookFormatException()
    }

    private suspend fun readSpineItemIds(xmlParser: XmlPullParser): List<String> {
        val spineItemIds = mutableListOf<String>()

        xmlParser.require(XmlPullParser.START_TAG, null, TAGS.SPINE)
        yield()

        while (!(xmlParser.next() == XmlPullParser.END_TAG && xmlParser.name == TAGS.SPINE)) {
            yield()

            if (xmlParser.eventType == XmlPullParser.START_TAG) {
                if (xmlParser.name == TAGS.ITEMREF) {
                    val spineItemId = xmlParser.getAttributeValue(null, "idref")
                    spineItemIds.add(spineItemId)
                }
            }
        }
        yield()

        xmlParser.require(XmlPullParser.END_TAG, null, TAGS.SPINE)
        yield()

        return spineItemIds
    }

    @Throws(DirBookFormatException::class)
    private suspend fun extractRelativePathsOfSpineItemFiles(
        bookDir: File,
        relativePathOfContentFile: String,
        spineItemIds: List<String>
    ): List<String> {
        val spineItemIdToRelativePathOfSpineItemFile = LinkedHashMap<String, String?>()
        for (spineItemId in spineItemIds) {
            spineItemIdToRelativePathOfSpineItemFile.put(spineItemId, null)
        }

        val contentFile = File(bookDir, relativePathOfContentFile)
        FileInputStream(contentFile).use { inputStream ->
            yield()

            val xmlParser: XmlPullParser = Xml.newPullParser()
            xmlParser.setInput(inputStream, null)
            yield()

            while (xmlParser.next() != XmlPullParser.END_DOCUMENT) {
                yield()

                if (xmlParser.eventType == XmlPullParser.START_TAG) {
                    if (xmlParser.name == TAGS.MANIFEST) {
                        readManifest(
                            xmlParser,
                            relativePathOfContentFile,
                            spineItemIdToRelativePathOfSpineItemFile)
                        break
                    }
                }
            }
        }
        yield()

        val relativePathsOfSpineItemFiles = spineItemIdToRelativePathOfSpineItemFile.values
        return relativePathsOfSpineItemFiles.map { it ?: throw DirBookFormatException() }
    }

    private suspend fun readManifest(
        xmlParser: XmlPullParser,
        relativePathOfContentFile: String,
        spineItemIdToRelativePathOfSpineItemFile: LinkedHashMap<String, String?>
    ) {
        xmlParser.require(XmlPullParser.START_TAG, null, TAGS.MANIFEST)
        yield()

        while (!(xmlParser.next() == XmlPullParser.END_TAG && xmlParser.name == TAGS.MANIFEST)) {
            yield()

            if (xmlParser.eventType == XmlPullParser.START_TAG) {
                if (xmlParser.name == TAGS.ITEM) {
                    val itemId = xmlParser.getAttributeValue(null, "id")
                    if (spineItemIdToRelativePathOfSpineItemFile.containsKey(itemId)) {
                        val itemReference = xmlParser.getAttributeValue(null, "href")
                        val relativeDirPath = File(relativePathOfContentFile).parent
                        val relativePathOfSpineItemFile = File(relativeDirPath, itemReference).path
                        spineItemIdToRelativePathOfSpineItemFile.put(
                            itemId, relativePathOfSpineItemFile)
                    }
                }
            }
        }
        yield()

        xmlParser.require(XmlPullParser.END_TAG, null, TAGS.MANIFEST)
        yield()
    }

    private object TAGS {
        const val ROOTFILE: String = "rootfile"

        const val METADATA: String = "metadata"
        const val IDENTIFIER: String = "identifier"
        const val TITLE: String = "title"

        const val MANIFEST: String = "manifest"
        const val ITEM: String = "item"

        const val SPINE: String = "spine"
        const val ITEMREF: String = "itemref"
    }
}
