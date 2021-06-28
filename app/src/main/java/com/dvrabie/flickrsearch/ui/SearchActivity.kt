package com.dvrabie.flickrsearch.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dvrabie.flickrsearch.*
import com.dvrabie.flickrsearch.coroutines.CoScopeImpl
import com.dvrabie.flickrsearch.data.Image
import com.dvrabie.flickrsearch.data.SearchRepositoryImpl
import com.dvrabie.flickrsearch.webclient.FlickrClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity(), Search.View {

    private var textChangedJob: Job? = null
    private var presenter: Search.Presenter? = null
    private val adapter = SearchAdapter()
    private var initialLabel: TextView? = null
    private var progressBar: ProgressBar? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()
        presenter = SearchPresenter(SearchRepositoryImpl(FlickrClient.client), this, CoScopeImpl())
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView?.let {
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                        presenter?.onScrolled(
                            visibleItemCount,
                            totalItemCount,
                            firstVisibleItemPosition
                        )
                    }
                }
            })
        }
    }

    private fun initViews() {
        initialLabel = findViewById(R.id.initial_label)
        progressBar = findViewById(R.id.progress)
        recyclerView = findViewById(R.id.searchRv)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem: MenuItem? = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        searchView.isIconifiedByDefault = true
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) presenter?.searchQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    textChangedJob?.cancel()
                    textChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                        delay(DEBOUNCE_DELAY)
                        presenter?.searchQuery(newText)
                    }
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
        recyclerView?.isVisible = true
        initialLabel?.isVisible = false
        adapter.submitList(ArrayList(images))
    }

    override fun showLoading(isLoading: Boolean) {
        progressBar?.isVisible = isLoading
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}