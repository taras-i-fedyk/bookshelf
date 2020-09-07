package com.tarasfedyk.example.bookshelf.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tarasfedyk.example.bookshelf.biz.models.SpineItem

abstract class SpineItemsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    abstract var items: List<SpineItem>?
}
