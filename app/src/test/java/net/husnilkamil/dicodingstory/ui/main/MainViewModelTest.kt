package net.husnilkamil.dicodingstory.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import net.husnilkamil.dicodingstory.data.StoryRepository
import net.husnilkamil.dicodingstory.data.entity.Story
import net.husnilkamil.dicodingstory.ui.DummyData
import net.husnilkamil.dicodingstory.ui.MainDispatcherRule
import net.husnilkamil.dicodingstory.ui.getOrAwaitValue
import net.husnilkamil.dicodingstory.ui.noopListUpdateCallback
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: StoryRepository

    @Test
    fun `when request data successful should get correct response`() = runTest {
        val dummyData = DummyData.genereteStoryEntity()
        val data : PagingData<Story> = PagingData.from(dummyData)
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data
        `when`(repository.getStories()).thenReturn(expectedStories)

        val mainViewModel = MainViewModel(repository)
        val actualStory: PagingData<Story> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyData, differ.snapshot())
        assertEquals(dummyData.size, differ.snapshot().size)
        assertEquals(dummyData[0].name, differ.snapshot()[0]?.name)

    }
}

