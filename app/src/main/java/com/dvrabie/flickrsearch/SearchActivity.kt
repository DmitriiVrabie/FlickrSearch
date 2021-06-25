package com.dvrabie.flickrsearch

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity(), Search.View {

    private var textChangedJob: Job? = null
    private var presenter: Search.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        presenter = SearchPresenter(SearchRepositoryImpl(FlickrClient.client), this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem: MenuItem? = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                textChangedJob?.cancel()
                textChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                    delay(DEBOUNCE_DELAY)
                    if (!newText.isNullOrEmpty()) presenter?.searchQuery(newText)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        presenter = null
        super.onDestroy()
    }

    override fun showSearchResult(images: List<Image>) {
        println("images: $images")
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}