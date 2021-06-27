package com.dvrabie.flickrsearch

import com.dvrabie.flickrsearch.FlickrClient.FLICKR_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickService {

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=$FLICKR_API_KEY")
    suspend fun searchImages(
        @Query("text") query: String,
        @Query("page") page: Int
    ): ImageSearchResponse
}