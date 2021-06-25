package com.dvrabie.flickrsearch

import android.widget.SearchView
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchTextWatcher(
    private val lifecycle: LifecycleCoroutineScope
) : SearchView.OnQueryTextListener {

    private var textChangedJob: Job? = null

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        textChangedJob?.cancel()
        textChangedJob = lifecycle.launch(Dispatchers.Main) {
            delay(DEBOUNCE_DELAY)
            println("Text $newText, Time: ${System.currentTimeMillis()}")
        }
        return true
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}