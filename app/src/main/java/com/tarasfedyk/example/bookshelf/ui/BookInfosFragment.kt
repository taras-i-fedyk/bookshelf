package com.tarasfedyk.example.bookshelf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tarasfedyk.example.bookshelf.biz.BookInfosVm
import com.tarasfedyk.example.bookshelf.biz.models.BookInfo
import com.tarasfedyk.example.bookshelf.databinding.BookInfosFragmentBinding
import com.tarasfedyk.example.bookshelf.ui.adapters.BookInfosAdapter
import com.tarasfedyk.example.bookshelf.ui.adapters.BookInfosAdapterFactory
import com.tarasfedyk.example.bookshelf.ui.adapters.AppendStateAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookInfosFragment : Fragment() {

    @Inject lateinit var bookInfosVm: BookInfosVm

    @Inject lateinit var bookInfosAdapterFactory: BookInfosAdapterFactory
    @Inject lateinit var bookInfosDiffCallback: DiffUtil.ItemCallback<BookInfo>
    private lateinit var bookInfosAdapter: BookInfosAdapter<out RecyclerView.ViewHolder>

    @Inject lateinit var appendStateAdapterFactory: AppendStateAdapterFactory
    private lateinit var appendStateAdapter: LoadStateAdapter<out RecyclerView.ViewHolder>

    private lateinit var concatAdapter: ConcatAdapter

    private val navController: NavController by lazy { findNavController() }

    private val isRefreshProgressBarVisibleLiveData: MutableLiveData<Boolean> = MutableLiveData()

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

            appendStateAdapter =
                appendStateAdapterFactory.createAppendStateAdapter { bookInfosAdapter.retry() }

            concatAdapter = bookInfosAdapter.withLoadStateFooter(appendStateAdapter)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bookInfosFragmentBinding =
            BookInfosFragmentBinding.inflate(inflater, container, false)

        bookInfosFragmentBinding.lifecycleOwner = this

        bookInfosFragmentBinding.navController = navController
        bookInfosFragmentBinding.appBarConfiguration = AppBarConfiguration(navController.graph)

        bookInfosFragmentBinding.isRefreshProgressBarVisibleLiveData =
            isRefreshProgressBarVisibleLiveData

        bookInfosFragmentBinding.concatAdapter = concatAdapter

        return bookInfosFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                bookInfosVm.flow.collectLatest { pagingData ->
                    bookInfosAdapter.submitData(pagingData)
                }
            }
            launch {
                bookInfosAdapter.loadStateFlow.collectLatest { loadStates ->
                    val refreshState = loadStates.refresh
                    val appendState = loadStates.append
                    isRefreshProgressBarVisibleLiveData.value =
                        refreshState is LoadState.Loading && appendState !is LoadState.Loading
                }
            }
        }
    }
}
