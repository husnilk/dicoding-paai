package net.husnilkamil.dicodingstory.ui.add

import androidx.lifecycle.ViewModel
import net.husnilkamil.dicodingstory.data.StoryRepository
import net.husnilkamil.dicodingstory.data.request.StoryRequest

class AddStoryViewModel (private val repository: StoryRepository): ViewModel(){
    fun uploadStory(storyRequest: StoryRequest) = repository.insert(storyRequest)
}