package com.tarasfedyk.example.bookshelf.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import com.tarasfedyk.example.bookshelf.biz.SpineItemsVm
import com.tarasfedyk.example.bookshelf.databinding.BookFragmentBinding
import com.tarasfedyk.example.bookshelf.ui.adapters.SpineItemsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.book_fragment.*
import kotlinx.coroutines.flow.collectIndexed
import javax.inject.Inject

@AndroidEntryPoint
class BookFragment : Fragment() {

    private val args: BookFragmentArgs by navArgs()

    @Inject lateinit var spineItemsVm: SpineItemsVm

    @Inject lateinit var spineItemsAdapter: SpineItemsAdapter

    private val navController: NavController by lazy { findNavController() }

    private val currentPageIndexLiveData: MutableLiveData<Int> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bookFragmentBinding =
            BookFragmentBinding.inflate(inflater, container, false)

        bookFragmentBinding.lifecycleOwner = this

        bookFragmentBinding.navController = navController
        bookFragmentBinding.appBarConfiguration = AppBarConfiguration(navController.graph)
        bookFragmentBinding.subtitle = args.bookInfo.title

        bookFragmentBinding.spineItemsAdapter = spineItemsAdapter
        bookFragmentBinding.offscreenPageLimit = OFFSCREEN_PAGE_LIMIT
        bookFragmentBinding.currentPageIndexLiveData = currentPageIndexLiveData

        return bookFragmentBinding.root
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scrollPositionToRestore: Int? =
            savedInstanceState?.getInt(KEY_SCROLL_POSITION)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            spineItemsVm.getFlow(args.bookInfo.id).collectIndexed { index, spineItems ->
                spineItemsAdapter.items = spineItems
                if (index == 0 && scrollPositionToRestore != null) {
                    currentPageIndexLiveData.value = scrollPositionToRestore
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_SCROLL_POSITION, view_pager.currentItem)
    }

    companion object {
        private const val KEY_SCROLL_POSITION: String = "scroll_position"

        private const val OFFSCREEN_PAGE_LIMIT: Int = 1
    }
}
