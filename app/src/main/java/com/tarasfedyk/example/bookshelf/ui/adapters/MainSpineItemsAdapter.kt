package com.tarasfedyk.example.bookshelf.ui.adapters

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.webkit.WebViewAssetLoader
import com.tarasfedyk.example.bookshelf.biz.models.SpineItem
import com.tarasfedyk.example.bookshelf.databinding.SpineItemFragmentBinding
import javax.inject.Inject
import kotlin.properties.Delegates

class MainSpineItemsAdapter @Inject constructor(
    fragment: Fragment
) : SpineItemsAdapter(fragment) {

    override var items: List<SpineItem>? by Delegates.observable(null) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun createFragment(position: Int): Fragment {
        val item = items!![position]
        return SpineItemFragment.newInstance(item)
    }
}

class SpineItemFragment : Fragment() {
    private lateinit var spineItem: SpineItem
    private lateinit var spineItemUrl: Uri

    private val isProgressBarVisibleLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val isWebViewVisibleLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var webViewAssetLoader: WebViewAssetLoader

    init {
        lifecycleScope.launchWhenCreated {
            spineItem = requireArguments().getParcelable(KEY_SPINE_ITEM)!!
            val bookDir = spineItem.bookDir
            val bookDirName = bookDir.name
            spineItemUrl = Uri
                .Builder()
                .scheme("https")
                .authority(WebViewAssetLoader.DEFAULT_DOMAIN)
                .appendPath(bookDirName)
                .appendEncodedPath(spineItem.relativeFilePath)
                .build()

            webViewAssetLoader = WebViewAssetLoader
                .Builder()
                .addPathHandler(
                    "/$bookDirName/",
                    WebViewAssetLoader.InternalStoragePathHandler(
                        requireContext(),
                        bookDir
                    )
                )
                .build()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val spineItemFragmentBinding =
            SpineItemFragmentBinding.inflate(inflater, container, false)

        spineItemFragmentBinding.lifecycleOwner = this

        spineItemFragmentBinding.isProgressBarVisibleLiveData = isProgressBarVisibleLiveData
        spineItemFragmentBinding.isWebViewVisibleLiveData = isWebViewVisibleLiveData

        isProgressBarVisibleLiveData.value = true
        isWebViewVisibleLiveData.value = false
        spineItemFragmentBinding.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    isProgressBarVisibleLiveData.value = false
                    isWebViewVisibleLiveData.value = true
                }
            }
        }
        spineItemFragmentBinding.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return webViewAssetLoader.shouldInterceptRequest(request.url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val link: Uri = Uri.parse(url)

                if (link.host == WebViewAssetLoader.DEFAULT_DOMAIN) {
                    return false
                }

                startActivity(Intent(Intent.ACTION_VIEW, link))
                return true
            }
        }
        spineItemFragmentBinding.isJavaScriptEnabled = true
        spineItemFragmentBinding.spineItemUrl = spineItemUrl

        spineItemFragmentBinding.executePendingBindings()

        return spineItemFragmentBinding.root
    }

    companion object {
        private const val KEY_SPINE_ITEM = "spine_item"

        fun newInstance(spineItem: SpineItem) =
            SpineItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_SPINE_ITEM, spineItem)
                }
            }
    }
}
