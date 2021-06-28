package com.dvrabie.flickrsearch.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoScope {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
}

class CoScopeImpl(
    private val dispatcher: Dispatchers = Dispatchers
) : CoScope {
    override fun io(): CoroutineDispatcher = dispatcher.IO
    override fun main(): CoroutineDispatcher = dispatcher.Main
}

class TestCoScope : CoScope {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined

}