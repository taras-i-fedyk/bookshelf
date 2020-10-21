package com.tarasfedyk.example.bookshelf.repo

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.paging.*
import androidx.work.*
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookInfo
import com.tarasfedyk.example.bookshelf.repo.inj.qualifiers.DbBooksFetcherClass
import com.tarasfedyk.example.bookshelf.repo.exceptions.DbBooksFetchException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.jvm.Throws

@OptIn(ExperimentalPagingApi::class)
class MainDbBooksMediator @Inject constructor (
    @ApplicationContext private val appContext: Context,
    @DbBooksFetcherClass private val dbBooksFetcherClass: Class<out ListenableWorker>
) : RemoteMediator<Int, DbBookInfo>() {

    private val workManager = WorkManager.getInstance(appContext)

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DbBookInfo>
    ): MediatorResult {
        if (loadType == LoadType.PREPEND) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        val ordinalOfLastAvailableDbBook =
            if (loadType == LoadType.REFRESH) {
                0
            } else {
                state.lastItemOrNull()?.ordinal ?: 0
            }
        val ordinalOfFirstNewDbBook = ordinalOfLastAvailableDbBook + 1
        val ordinalOfLastNewDbBook = ordinalOfLastAvailableDbBook + BooksRepoConstants.DIR_PAGE_SIZE
        try {
            val areMoreDbBooksAvailable =
                fetchDbBooks(ordinalOfFirstNewDbBook, ordinalOfLastNewDbBook)
            return MediatorResult.Success(endOfPaginationReached = !areMoreDbBooksAvailable)
        } catch (e: DbBooksFetchException) {
            return MediatorResult.Error(e)
        } 
    }

    @Throws(DbBooksFetchException::class)
    private suspend fun fetchDbBooks(firstDbBookOrdinal: Int, lastDbBookOrdinal: Int): Boolean {
        val uniqueWorkName = "fetchDbBooks"

        val workRequest = OneTimeWorkRequest
            .Builder(dbBooksFetcherClass)
            .setInputData(
                workDataOf(
                    BooksRepoKeys.FIRST_DB_BOOK_ORDINAL to firstDbBookOrdinal,
                    BooksRepoKeys.LAST_DB_BOOK_ORDINAL to lastDbBookOrdinal
                )
            )
            .build()
        workManager.enqueueUniqueWork(uniqueWorkName, ExistingWorkPolicy.KEEP, workRequest).await()

        val workStatesFlow = workManager.getWorkInfosForUniqueWorkLiveData(uniqueWorkName).asFlow()
            .map { workInfos -> workInfos[0].state }
        val finalWorkState =
            workStatesFlow
                .filter { workState ->
                    workState == WorkInfo.State.SUCCEEDED ||
                    workState == WorkInfo.State.FAILED ||
                    workState == WorkInfo.State.CANCELLED
                }
                .first()
        if (finalWorkState == WorkInfo.State.FAILED || finalWorkState == WorkInfo.State.CANCELLED) {
            throw DbBooksFetchException()
        }
        val workInfo = workManager.getWorkInfosForUniqueWork(uniqueWorkName).await()[0]
        return workInfo.outputData.getBoolean(
            BooksRepoKeys.ARE_MORE_DB_BOOKS_AVAILABLE, true)
    }
}
