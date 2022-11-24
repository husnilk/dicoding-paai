package net.husnilkamil.dicodingstory.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers
import net.husnilkamil.dicodingstory.data.StoryRepository
import net.husnilkamil.dicodingstory.data.entity.Story
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{

    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var mainViewModel: MainViewModel
    private val dummyData = DummyData.genereteStoryEntity()

    @Before
    fun setUp(){
        mainViewModel = MainViewModel(repository)
    }

    @Test
    fun `this is a test`(){
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = PagingData.from(dummyData)
        `when`(repository.getStories()).thenReturn(expectedStories);

        val actualStory: PagingData<Story> = mainViewModel.stories.value!!

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyData, differ.snapshot())
        assertEquals(dummyData.size, differ.snapshot().size)

    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}