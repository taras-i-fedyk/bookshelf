package com.tarasfedyk.example.bookshelf.repo

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.work.*
import com.tarasfedyk.example.bookshelf.repo.db.models.DbBookInfo
import com.tarasfedyk.example.bookshelf.repo.inj.qualifiers.DbBooksPreparerClass
import com.tarasfedyk.example.bookshelf.repo.exceptions.DbBooksPreparationException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.jvm.Throws

@OptIn(ExperimentalPagingApi::class)
class DbBooksMediator @Inject constructor (
    @ApplicationContext private val appContext: Context,
    @DbBooksPreparerClass private val dbBooksPreparerClass: Class<out ListenableWorker>
) : RemoteMediator<Int, DbBookInfo>() {

    private val workManager = WorkManager.getInstance(appContext)

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DbBookInfo>
    ): MediatorResult =
        when (loadType) {
            LoadType.REFRESH ->
                try {
                    prepareDbBooks()
                    MediatorResult.Success(endOfPaginationReached = true)
                } catch (e: DbBooksPreparationException) {
                    MediatorResult.Error(e)
                }
            LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> MediatorResult.Success(endOfPaginationReached = true)
        }

    @Throws(DbBooksPreparationException::class)
    suspend fun prepareDbBooks() {
        val uniqueWorkName = "prepareDbBooks"

        val workRequest = OneTimeWorkRequest.from(dbBooksPreparerClass)
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
            throw DbBooksPreparationException()
        }
    }
}
