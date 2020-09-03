package com.tarasfedyk.example.bookshelf.repo

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.*
import com.tarasfedyk.example.bookshelf.repo.di.qualifiers.BooksPreparerClass
import com.tarasfedyk.example.bookshelf.repo.exceptions.BooksPreparationException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.jvm.Throws

class BooksMediator @Inject constructor (
    @ApplicationContext private val appContext: Context,
    @BooksPreparerClass private val booksPreparerClass: Class<out ListenableWorker>
) {

    private val workManager = WorkManager.getInstance(appContext)

    @Throws(BooksPreparationException::class)
    suspend fun prepareBooks() {
        val uniqueWorkName = "prepareBooks"

        val workRequest = OneTimeWorkRequest.from(booksPreparerClass)
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
            throw BooksPreparationException()
        }
    }
}
