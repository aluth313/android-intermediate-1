package com.aluth.storyapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aluth.storyapp.data.model.response.Story
import com.aluth.storyapp.data.network.ApiService

class StoryPagingSource(
    private val apiService: ApiService,
    private val token: String,
) : PagingSource<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.stories("Bearer $token", page, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1,
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}