package com.dvrabie.flickrsearch.data

interface SearchRepository {
    suspend fun loadImages(query: String, page: Int): ImagesData
}