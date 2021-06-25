package com.dvrabie.flickrsearch

interface Search {
    interface Presenter {
        fun searchQuery(query: String)
    }

    interface View {
        fun showSearchResult(images: List<Image>)
    }
}