package com.dvrabie.flickrsearch

data class Image(
    val id: String,
    val url: String
)

data class ImageSearchResponse(
    val images: ImagesData
)

data class ImagesData(
    val page: Int,
    val images: List<Image>
)

data class FlickrImage(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int
) {
    fun imageUrl(): String =
        "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}.jpg"
}