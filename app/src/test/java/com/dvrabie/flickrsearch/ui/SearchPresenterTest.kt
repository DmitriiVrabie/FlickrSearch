package com.dvrabie.flickrsearch.ui

import com.dvrabie.flickrsearch.coroutines.TestCoScope
import com.dvrabie.flickrsearch.data.FlickrImage
import com.dvrabie.flickrsearch.data.Image
import com.dvrabie.flickrsearch.data.ImagesData
import com.dvrabie.flickrsearch.data.SearchRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SearchPresenterTest {
    private lateinit var presenter: SearchPresenter

    private lateinit var repo: SearchRepository

    private lateinit var view: Search.View

    @Before
    fun setup() {
        repo = mockk(relaxed = true)
        view = mockk(relaxed = true)
        presenter = SearchPresenter(repo, view, TestCoScope())
    }

    @Test
    fun `load images with success`() {
        val image = FlickrImage(
            "id", "owner", "secret", "server", 6
        )
        val mockImages = listOf(image)
        val result = mockk<ImagesData>(relaxed = true) {
            every { flickrImages } returns mockImages
        }

        coEvery { repo.loadImages(any(), any()) } returns result
        val slot = slot<List<Image>>()
        runBlocking {
            presenter.searchQuery("dogo")
        }
        coVerify {
            view.showLoading(true)
            view.showSearchResult(capture(slot))
            view.showLoading(false)
        }

        slot.captured.first().apply {
            assertEquals(id, "id")
            assertEquals(url, "https://farm6.staticflickr.com/server/id_secret.jpg")
        }
    }

}