package com.dvrabie.flickrsearch

class SearchRepositoryImpl(
    private val service: FlickService
) : SearchRepository {

    override suspend fun loadImages(query: String, page: Int): ImagesData {
        return service.searchImages(query, page).images
    }
}