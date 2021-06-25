package com.dvrabie.flickrsearch

interface SearchRepository {
    fun loadImages(query: String): List<ImagesData>
}