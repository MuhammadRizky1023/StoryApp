package com.example.storyaipp.ui.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.API.StoryApiConfig
import com.example.storyapp.Model.ListStory
import com.example.storyapp.Model.ResponseStory
import retrofit2.Call
import retrofit2.Response

class ListStoryViewModel: ViewModel() {
    var listStoriesUser: List<ListStory> = listOf()
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var isError: Boolean = false


    fun getStories(token: String) {
        _isLoading.value = true
        val api = StoryApiConfig.getApiStoriesConfig().getStoriesUser("Bearer $token")
        api.enqueue(object : retrofit2.Callback<ResponseStory> {
            override fun onResponse(call: Call<ResponseStory>, response: Response<ResponseStory>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    isError = false
                    val responseBody = response.body()
                    if (responseBody !== null ) {
                        listStoriesUser= responseBody.listStory
                    }
                    _message.value = responseBody?.message.toString()

                } else {
                    isError = true
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ResponseStory>, t: Throwable) {
                _isLoading.value = false
                isError = true
                _message.value = t.message.toString()
            }

        })
    }
}