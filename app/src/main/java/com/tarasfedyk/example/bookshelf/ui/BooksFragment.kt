package com.tarasfedyk.example.bookshelf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.R
import com.tarasfedyk.example.bookshelf.biz.BookMetadatasVm
import com.tarasfedyk.example.bookshelf.biz.di.qualifiers.BookMetadatasVmFactory
import com.tarasfedyk.example.bookshelf.biz.models.BookMetadata
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.books_fragment.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BooksFragment : Fragment() {

    @Inject @BookMetadatasVmFactory lateinit var bookMetadatasVmFactory: ViewModelProvider.Factory
    private val bookMetadatasVm: BookMetadatasVm by viewModels { bookMetadatasVmFactory }

    @Inject
    lateinit var bookMetadatasAdapter: PagingDataAdapter<BookMetadata, out RecyclerView.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.books_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = bookMetadatasAdapter
        }
        refresh_retry_button.setOnClickListener { bookMetadatasAdapter.retry() }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                bookMetadatasVm.flow.collectLatest { pagingData ->
                    bookMetadatasAdapter.submitData(pagingData)
                }
            }
            launch {
                bookMetadatasAdapter.loadStateFlow.collectLatest { loadStates ->
                    val refreshState = loadStates.refresh
                    refresh_progress_bar.isVisible = refreshState is LoadState.Loading
                    refresh_retry_button.isVisible = refreshState is LoadState.Error
                    refresh_error_message_view.isVisible = refreshState is LoadState.Error
                }
            }
        }
    }
}
