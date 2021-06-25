package com.dvrabie.flickrsearch

class SearchRepositoryImpl(
    service: FlickService
) : SearchRepository {

    override fun loadImages(query: String): List<ImagesData> {
        return emptyList()
    }
}