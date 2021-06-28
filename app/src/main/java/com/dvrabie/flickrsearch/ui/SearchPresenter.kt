package com.dvrabie.flickrsearch.ui

import com.dvrabie.flickrsearch.coroutines.CoScope
import com.dvrabie.flickrsearch.data.Image
import com.dvrabie.flickrsearch.data.SearchRepository
import com.dvrabie.flickrsearch.data.toImages
import kotlinx.coroutines.*

class SearchPresenter(
    private val repository: SearchRepository,
    private val view: Search.View,
    private val coScope: CoScope
) : Search.Presenter {

    private var currentPage = 1
    private var query: String = ""
    private var loading: Boolean = false
    private var lastPage: Int = Integer.MAX_VALUE
    private val scope = CoroutineScope(SupervisorJob())
    private var images = ArrayList<Image>(ITEMS_PER_PAGE)

    private val isLastPage: Boolean
        get() = currentPage >= lastPage

    override fun searchQuery(query: String) {
        images.clear()
        this.query = query
        currentPage = 1
        searchQuery(query, currentPage)
    }

    private fun searchQuery(query: String, page: Int) {
        updateLoading(true)
        io(onSuccess = {
            lastPage = it.pages
            images.addAll(it.flickrImages.toImages())
            view.showSearchResult(images)
            updateLoading(false)
        }, onError = { println(it.message) }) {
            repository.loadImages(query, page)
        }
    }

    override fun onScrolled(
        visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int
    ) {
        if (!loading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                firstVisibleItemPosition >= FIRST_ITEM &&
                totalItemCount >= ITEMS_PER_PAGE
            ) {
                searchQuery(query, currentPage++)
            }
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        loading = isLoading
        view.showLoading(loading)
    }

    private fun <T> io(
        dispatcher: CoroutineDispatcher = coScope.io(),
        onError: (Throwable) -> Unit,
        onSuccess: (T) -> Unit,
        doOnComplete: () -> Unit = {},
        block: suspend () -> T
    ) {
        scope.launch(dispatcher) {
            try {
                val result = block()
                withContext(coScope.main()) {
                    onSuccess(result)
                }
            } catch (t: Throwable) {
                onError(t)
            } finally {
                doOnComplete()
            }
        }
    }

    companion object {

        private const val FIRST_ITEM = 0
        private const val ITEMS_PER_PAGE = 100
    }
}