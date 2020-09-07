package com.tarasfedyk.example.bookshelf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.R
import com.tarasfedyk.example.bookshelf.biz.BookInfosVm
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import com.tarasfedyk.example.bookshelf.ui.adapters.BookInfosAdapter
import com.tarasfedyk.example.bookshelf.ui.adapters.BookInfosAdapterFactory
import com.tarasfedyk.example.bookshelf.ui.adapters.inj.qualifiers.BookInfosDiffCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.book_infos_fragment.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookInfosFragment : Fragment() {

    @Inject lateinit var bookInfosVm: BookInfosVm

    @Inject
    lateinit var bookInfosAdapterFactory: BookInfosAdapterFactory
    @Inject
    @BookInfosDiffCallback
    lateinit var bookInfosDiffCallback: DiffUtil.ItemCallback<BookInfo>
    private lateinit var bookInfosAdapter: BookInfosAdapter<out RecyclerView.ViewHolder>

    private val navController: NavController by lazy { findNavController() }

    init {
        lifecycleScope.launchWhenCreated {
            val onItemClickListener = BookInfosAdapter.OnItemClickListener { item ->
                val actionBookInfosFragmentToBookFragment =
                    BookInfosFragmentDirections.actionBookInfosFragmentToBookFragment(item)
                navController.navigate(actionBookInfosFragmentToBookFragment)
            }
            bookInfosAdapter =
                bookInfosAdapterFactory.createBookInfosAdapter(
                    bookInfosDiffCallback,
                    onItemClickListener
                )
            bookInfosAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_infos_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = bookInfosAdapter
        }
        refresh_retry_button.setOnClickListener { bookInfosAdapter.retry() }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                bookInfosVm.flow.collectLatest { pagingData ->
                    bookInfosAdapter.submitData(pagingData)
                }
            }
            launch {
                bookInfosAdapter.loadStateFlow.collectLatest { loadStates ->
                    val refreshState = loadStates.refresh
                    refresh_progress_bar.isVisible = refreshState is LoadState.Loading
                    refresh_retry_button.isVisible = refreshState is LoadState.Error
                    refresh_error_message_view.isVisible = refreshState is LoadState.Error
                }
            }
        }
    }
}
