package com.dvrabie.flickrsearch.ui

import com.dvrabie.flickrsearch.data.Image

interface Search {
    interface Presenter {
        fun searchQuery(query: String)
        fun onScrolled(visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int)
    }

    interface View {
        fun showSearchResult(images: List<Image>)
        fun showLoading(isLoading: Boolean)
    }
}