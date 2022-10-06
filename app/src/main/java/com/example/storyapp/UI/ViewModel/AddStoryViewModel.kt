package com.example.storyapp.UI.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.API.StoryApiConfig
import com.example.storyapp.Model.ResponseMessage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel: ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun upload(photo: MultipartBody.Part, description: RequestBody, token: String) {
        _isLoading.value = true
        val service = StoryApiConfig.getApiStoriesConfig().uploadImageStories(
            photo, description,
            "Bearer $token"
        )
        service.enqueue(object : Callback<ResponseMessage> {
            override fun onResponse(
                call: Call<ResponseMessage>,
                response: Response<ResponseMessage>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _message.value = responseBody.message
                    }
                } else {
                    _message.value = response.message()

                }
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Failed Retrofit Instance"
            }
        })
    }
}