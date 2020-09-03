package com.tarasfedyk.example.bookshelf.repo.raw

import android.util.Xml
import com.tarasfedyk.example.bookshelf.repo.raw.exceptions.BookFormatException
import com.tarasfedyk.example.bookshelf.repo.raw.models.RawBookDetails
import com.tarasfedyk.example.bookshelf.repo.raw.models.RawBookInfo
import com.tarasfedyk.example.bookshelf.repo.raw.models.RawBookMetadata
import kotlinx.coroutines.yield
import org.xmlpull.v1.XmlPullParser
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject
import kotlin.jvm.Throws

class MainBookParser @Inject constructor() : BookParser {

    @Throws(BookFormatException::class)
    override suspend fun extractBookDetails(bookDir: File): RawBookDetails {
        val relativeContentFilePath = extractRelativeContentFilePath(bookDir)
        val bookInfo = extractBookInfo(bookDir, relativeContentFilePath)
        val relativeSpineFilePaths =
            extractRelativeSpineFilePaths(bookDir, relativeContentFilePath, bookInfo.spineFileIds)
        return RawBookDetails(bookInfo.metadata, relativeSpineFilePaths)
    }

    @Throws(BookFormatException::class)
    private suspend fun extractRelativeContentFilePath(bookDir: File): String {
        var relativeContentFilePath: String? = null

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
                            relativeContentFilePath = relativeFilePath
                            break
                        }
                    }
                }
            }
        }
        yield()

        return relativeContentFilePath ?: throw BookFormatException()
    }

    @Throws(BookFormatException::class)
    private suspend fun extractBookInfo(
        bookDir: File,
        relativeContentFilePath: String
    ): RawBookInfo {
        var bookMetadata: RawBookMetadata? = null
        var spineFileIds: List<String>? = null

        val contentFile = File(bookDir, relativeContentFilePath)
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
                            spineFileIds = readSpineFileIds(xmlParser)
                            break
                        }
                    }
                }
            }
        }
        yield()

        return RawBookInfo(
            bookMetadata ?: throw BookFormatException(),
            spineFileIds ?: throw BookFormatException())
    }

    @Throws(BookFormatException::class)
    private suspend fun readBookMetadata(xmlParser: XmlPullParser): RawBookMetadata {
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

        return RawBookMetadata(
            id ?: throw BookFormatException(),
            title ?: throw BookFormatException()
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

    private suspend fun readSpineFileIds(xmlParser: XmlPullParser): List<String> {
        val spineFileIds = mutableListOf<String>()

        xmlParser.require(XmlPullParser.START_TAG, null, TAGS.SPINE)
        yield()

        while (!(xmlParser.next() == XmlPullParser.END_TAG && xmlParser.name == TAGS.SPINE)) {
            yield()

            if (xmlParser.eventType == XmlPullParser.START_TAG) {
                if (xmlParser.name == TAGS.ITEMREF) {
                    val spineFileId = xmlParser.getAttributeValue(null, "idref")
                    spineFileIds.add(spineFileId)
                }
            }
        }
        yield()

        xmlParser.require(XmlPullParser.END_TAG, null, TAGS.SPINE)
        yield()

        return spineFileIds
    }

    @Throws(BookFormatException::class)
    private suspend fun extractRelativeSpineFilePaths(
        bookDir: File,
        relativeContentFilePath: String,
        spineFileIds: List<String>
    ): List<String> {
        val spineFileIdToRelativeSpineFilePath = LinkedHashMap<String, String?>()
        for (spineFileId in spineFileIds) {
            spineFileIdToRelativeSpineFilePath.put(spineFileId, null)
        }

        val contentFile = File(bookDir, relativeContentFilePath)
        FileInputStream(contentFile).use { inputStream ->
            yield()

            val xmlParser: XmlPullParser = Xml.newPullParser()
            xmlParser.setInput(inputStream, null)
            yield()

            while (xmlParser.next() != XmlPullParser.END_DOCUMENT) {
                yield()

                if (xmlParser.eventType == XmlPullParser.START_TAG) {
                    if (xmlParser.name == TAGS.MANIFEST) {
                        readManifest(xmlParser, spineFileIdToRelativeSpineFilePath)
                        break
                    }
                }
            }
        }
        yield()

        val relativeSpineFilePaths = spineFileIdToRelativeSpineFilePath.values
        return relativeSpineFilePaths.map { it ?: throw BookFormatException() }
    }

    private suspend fun readManifest(
        xmlParser: XmlPullParser,
        spineFileIdToRelativeSpineFilePath: LinkedHashMap<String, String?>
    ) {
        xmlParser.require(XmlPullParser.START_TAG, null, TAGS.MANIFEST)
        yield()

        while (!(xmlParser.next() == XmlPullParser.END_TAG && xmlParser.name == TAGS.MANIFEST)) {
            yield()

            if (xmlParser.eventType == XmlPullParser.START_TAG) {
                if (xmlParser.name == TAGS.ITEM) {
                    val itemId = xmlParser.getAttributeValue(null, "id")
                    if (spineFileIdToRelativeSpineFilePath.containsKey(itemId)) {
                        val itemFilePath = xmlParser.getAttributeValue(null, "href")
                        spineFileIdToRelativeSpineFilePath.put(itemId, itemFilePath)
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
