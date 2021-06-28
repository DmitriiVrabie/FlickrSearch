package com.dvrabie.flickrsearch.data

import com.google.gson.annotations.SerializedName

data class Image(
    val id: String,
    val url: String
)

data class ImageSearchResponse(
    @SerializedName("photos")
    val images: ImagesData
)

data class ImagesData(
    val page: Int,
    val pages: Int,
    @SerializedName("photo")
    val flickrImages: List<FlickrImage>
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

fun List<FlickrImage>.toImages(): List<Image> = map { Image(it.id, it.imageUrl()) }