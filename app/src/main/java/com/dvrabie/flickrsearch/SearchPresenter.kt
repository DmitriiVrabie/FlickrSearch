package com.dvrabie.flickrsearch

import kotlinx.coroutines.*

class SearchPresenter(
    private val repository: SearchRepository,
    private val view: Search.View
) : Search.Presenter {
    private var currentPage = 1

    private val scope = CoroutineScope(SupervisorJob())

    override fun searchQuery(query: String) {
        scope.launch(Dispatchers.IO) {
            val imageResponse = repository.loadImages(query, currentPage)
            currentPage = imageResponse.page
            Dispatchers.Main {
                view.showSearchResult(imageResponse.flickrImages.toImages())
            }
        }
    }
}