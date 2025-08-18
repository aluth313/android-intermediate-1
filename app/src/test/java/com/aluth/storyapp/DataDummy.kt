package com.aluth.storyapp

import com.aluth.storyapp.data.model.response.Story
import com.aluth.storyapp.utils.formatTimeNow
import com.aluth.storyapp.utils.generateStoryId

object DataDummy {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                "https://m.media-amazon.com/images/I/511y5gjyKuL._UF894,1000_QL80_.jpg",
                formatTimeNow("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
                "Cerita 1",
                "Deskripsi Cerita 1",
                -10.212,
                generateStoryId(),
                -16.002,
            )
            items.add(story)
        }
        return items
    }
}