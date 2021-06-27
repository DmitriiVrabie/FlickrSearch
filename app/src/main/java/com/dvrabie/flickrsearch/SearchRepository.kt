package com.dvrabie.flickrsearch

interface SearchRepository {
    suspend fun loadImages(query: String, page: Int): ImagesData
}