package com.tarasfedyk.example.bookshelf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tarasfedyk.example.bookshelf.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}
