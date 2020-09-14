package com.tarasfedyk.example.bookshelf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.R
import com.tarasfedyk.example.bookshelf.databinding.AppendStateViewBinding

class MainAppendStateAdapter constructor(
    retry: () -> Unit
) : AppendStateAdapter<AppendStateViewHolder>(retry) {

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

    private val appendStateViewBinding = AppendStateViewBinding.bind(itemView)

    init {
        appendStateViewBinding.retryButtonClickCallback = View.OnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
        appendStateViewBinding.isProgressBarVisible = loadState is LoadState.Loading
        appendStateViewBinding.isErrorMessageVisible = loadState is LoadState.Error
        appendStateViewBinding.isRetryButtonVisible = loadState is LoadState.Error
        appendStateViewBinding.executePendingBindings()
    }
}
