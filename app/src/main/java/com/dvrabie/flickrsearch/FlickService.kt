package com.dvrabie.flickrsearch

import com.dvrabie.flickrsearch.FlickrClient.FLICKR_API_KEY
import retrofit2.http.GET

interface FlickService {

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1&text=dogs&api_key=$FLICKR_API_KEY")
    fun searchImages(query: String): List<ImageSearchResponse>
}