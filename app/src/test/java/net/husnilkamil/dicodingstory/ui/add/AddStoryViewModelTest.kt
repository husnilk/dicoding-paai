package net.husnilkamil.dicodingstory.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import net.husnilkamil.dicodingstory.data.StoryRepository
import net.husnilkamil.dicodingstory.data.request.StoryRequest
import net.husnilkamil.dicodingstory.data.response.InsertResponse
import net.husnilkamil.dicodingstory.ui.DummyData
import net.husnilkamil.dicodingstory.ui.getOrAwaitValue
import net.husnilkamil.dicodingstory.utils.getToken
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyResponse = DummyData.generateDummyInsertResponse()

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(repository)
    }

    @Test
    fun `when add story successful should return correct response`() {
        val expectedData = MutableLiveData<net.husnilkamil.dicodingstory.data.Result<InsertResponse>>()
        expectedData.value = net.husnilkamil.dicodingstory.data.Result.Success(dummyResponse)

        val uploadFile = File.createTempFile("jpg", "jpg")
        val dummyToken = "token"
        val dummyDescription = "description"
        val storyRequest = StoryRequest(dummyToken, dummyDescription, uploadFile)
        `when`(repository.insert(storyRequest)).thenReturn(expectedData)

        val actualData = addStoryViewModel.uploadStory(storyRequest).getOrAwaitValue()

        Mockito.verify(repository).insert(storyRequest)

        assertNotNull(actualData)
        assertTrue(actualData is net.husnilkamil.dicodingstory.data.Result.Success)
        assertEquals(dummyResponse.error, (actualData as net.husnilkamil.dicodingstory.data.Result.Success).data.error)
        assertEquals(dummyResponse.message, (actualData as net.husnilkamil.dicodingstory.data.Result.Success).data.message)
    }

    @Test
    fun `when add story unsuccessful should Return Error`() {
        val expectedResponse = MutableLiveData<net.husnilkamil.dicodingstory.data.Result<InsertResponse>>()
        expectedResponse.value = net.husnilkamil.dicodingstory.data.Result.Error("Error")

        var uploadFile = File.createTempFile("jpg", "jpg")
        val dummyToken = "token"
        val dummyDescription = "description"
        val storyRequest = StoryRequest(dummyToken, dummyDescription, uploadFile)
        `when`(repository.insert(storyRequest)).thenReturn(expectedResponse)

        val actualData = addStoryViewModel.uploadStory(storyRequest).getOrAwaitValue()
        Mockito.verify(repository).insert(storyRequest)

        assertNotNull(actualData)
        assertTrue(actualData is net.husnilkamil.dicodingstory.data.Result.Error)
    }


}