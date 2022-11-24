package net.husnilkamil.dicodingstory.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import net.husnilkamil.dicodingstory.data.StoryRepository
import net.husnilkamil.dicodingstory.data.entity.Story

class MainViewModel(repository: StoryRepository) : ViewModel() {

    val stories: LiveData<PagingData<Story>> =
        repository.getStories().cachedIn(viewModelScope)
}