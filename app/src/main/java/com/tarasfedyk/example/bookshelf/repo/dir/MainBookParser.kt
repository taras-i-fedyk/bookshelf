package com.tarasfedyk.example.bookshelf.repo.dir

import android.util.Xml
import com.tarasfedyk.example.bookshelf.repo.dir.exceptions.BookFormatException
import com.tarasfedyk.example.bookshelf.repo.dir.models.DirBookDetails
import com.tarasfedyk.example.bookshelf.repo.dir.models.DirBookInfo
import com.tarasfedyk.example.bookshelf.repo.dir.models.DirBookMetadata
import kotlinx.coroutines.yield
import org.xmlpull.v1.XmlPullParser
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject
import kotlin.jvm.Throws

class MainBookParser @Inject constructor() : BookParser {

    @Throws(BookFormatException::class)
    override suspend fun extractBookDetails(bookDir: File): DirBookDetails {
        val relativePathOfContentFile = extractRelativePathOfContentFile(bookDir)
        val bookInfo = extractBookInfo(bookDir, relativePathOfContentFile)
        val relativePathsOfSpineItemFiles =
            extractRelativePathsOfSpineItemFiles(
                bookDir, relativePathOfContentFile, bookInfo.spineItemIds)
        return DirBookDetails(
            metadata = bookInfo.metadata,
            relativePathsOfSpineItemFiles = relativePathsOfSpineItemFiles
        )
    }

    @Throws(BookFormatException::class)
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

        return relativePathOfContentFile ?: throw BookFormatException()
    }

    @Throws(BookFormatException::class)
    private suspend fun extractBookInfo(
        bookDir: File,
        relativePathOfContentFile: String
    ): DirBookInfo {
        var bookMetadata: DirBookMetadata? = null
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
                        TAGS.METADATA -> bookMetadata = readBookMetadata(xmlParser)
                        TAGS.SPINE -> {
                            spineItemIds = readSpineItemIds(xmlParser)
                            break
                        }
                    }
                }
            }
        }
        yield()

        return DirBookInfo(
            metadata = bookMetadata ?: throw BookFormatException(),
            spineItemIds = spineItemIds ?: throw BookFormatException()
        )
    }

    @Throws(BookFormatException::class)
    private suspend fun readBookMetadata(xmlParser: XmlPullParser): DirBookMetadata {
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

        return DirBookMetadata(
            id = id ?: throw BookFormatException(),
            title = title ?: throw BookFormatException()
        )
    }

    @Throws(BookFormatException::class)
    private suspend fun readText(xmlParser: XmlPullParser): String {
        var text: String? = null

        if (xmlParser.next() == XmlPullParser.TEXT) {
            yield()

            text = xmlParser.text
            xmlParser.next()
            yield()
        }

        return text ?: throw BookFormatException()
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

    @Throws(BookFormatException::class)
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
        return relativePathsOfSpineItemFiles.map { it ?: throw BookFormatException() }
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
