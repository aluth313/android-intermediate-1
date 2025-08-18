package com.aluth.storyapp.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.aluth.storyapp.DataDummy
import com.aluth.storyapp.MainDispatcherRule
import com.aluth.storyapp.data.local.datastore.SessionPreferences
import com.aluth.storyapp.data.model.response.LoginResult
import com.aluth.storyapp.data.model.response.Story
import com.aluth.storyapp.data.repository.StoryRepository
import com.aluth.storyapp.getOrAwaitValue
import com.aluth.storyapp.ui.core.PreferencesViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instanceExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var sessionPreferences: SessionPreferences
    private lateinit var preferencesViewModel: PreferencesViewModel

    @Before
    fun setup() {
        preferencesViewModel = PreferencesViewModel(sessionPreferences)

        val dummySession = "{\"token\":\"dummy_token_123\"}"
        Mockito.`when`(sessionPreferences.getUserSession())
            .thenReturn(flowOf(dummySession))
    }

    @Test
    fun `when Get Story Should Not Null and return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val dummyToken = "dummy_token_123"
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories(dummyToken)).thenReturn(expectedStory)

        val session = preferencesViewModel.getUserSession().getOrAwaitValue()
        val user = Gson().fromJson(session, LoginResult::class.java)
        val token = user?.token ?: ""
        val viewModel = StoryViewModel(storyRepository)
        val actualStory: PagingData<Story> = viewModel.getStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<Story>>()
        expectedQuote.value = data
        val dummyToken = "dummy_token_123"
        Mockito.`when`(storyRepository.getStories(dummyToken)).thenReturn(expectedQuote)
        val session = preferencesViewModel.getUserSession().getOrAwaitValue()
        val viewModel = StoryViewModel(storyRepository)
        val user = Gson().fromJson(session, LoginResult::class.java)
        val token = user?.token ?: ""
        val actualQuote: PagingData<Story> = viewModel.getStories(token).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
