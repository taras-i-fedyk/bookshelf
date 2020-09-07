package com.tarasfedyk.example.bookshelf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.R
import kotlinx.android.synthetic.main.append_state_view.view.*

class MainLoadStateAdapter constructor(
    private val retry: () -> Unit
) : LoadStateAdapter<AppendStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = AppendStateViewHolder(parent, retry)

    override fun onBindViewHolder(
        holder: AppendStateViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)
}

class AppendStateViewHolder(
    parent: ViewGroup,
    retry: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.append_state_view, parent, false)
) {

    private val progressBar: ProgressBar = itemView.progress_bar
    private val errorMessageView: TextView = itemView.error_message_view
    private val retryButton: Button = itemView.retry_button

    init {
        retryButton.apply { setOnClickListener { retry() } }
    }

    fun bind(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
        retryButton.isVisible = loadState is LoadState.Error
        errorMessageView.isVisible = loadState is LoadState.Error
    }
}
