package com.tarasfedyk.example.bookshelf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.tarasfedyk.example.bookshelf.R
import com.tarasfedyk.example.bookshelf.repo.DbBooksMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var dbBooksMediator: DbBooksMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            measureTimeMillis { dbBooksMediator.prepareDbBooks() }
                .also { println("XXX_YYY elapsed time = $it") }
        }
    }
}
