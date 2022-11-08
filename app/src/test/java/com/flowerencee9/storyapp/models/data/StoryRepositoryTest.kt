package com.flowerencee9.storyapp.models.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.flowerencee9.storyapp.DataDummy
import com.flowerencee9.storyapp.MainDispatcherRule
import com.flowerencee9.storyapp.getOrAwaitValue
import com.flowerencee9.storyapp.models.response.Story
import com.flowerencee9.storyapp.screens.main.AdapterItemData
import com.flowerencee9.storyapp.screens.main.MainViewModel
import com.flowerencee9.storyapp.screens.main.StoryPagingSource
import com.flowerencee9.storyapp.screens.main.noopListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `verify getStory shouldn't null`() = runTest {
        val dummyStory = DataDummy.generateDummyQuoteResponse()
        val data : PagingData<Story> = StoryPagingSource.snapshot(dummyStory)

        val expectedQuote = MutableLiveData<PagingData<Story>>()
        expectedQuote.value = data

        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedQuote)
        val mapsViewModel = MainViewModel(storyRepository)
        val actualStory: PagingData<Story> = mapsViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = AdapterItemData.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory, differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }
}