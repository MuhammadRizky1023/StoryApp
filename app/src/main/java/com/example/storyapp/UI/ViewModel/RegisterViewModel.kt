package com.example.storyapp.UI.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.API.StoryApiConfig
import com.example.storyapp.Model.RequestRegister
import com.example.storyapp.Model.ResponseMessage
import retrofit2.Call
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = _messages
    private val _isLoadingActive = MutableLiveData<Boolean>()
    val isLoadingActive: LiveData<Boolean> = _isLoadingActive
    var isErrorMessage: Boolean = false


    fun getUserResponseRegisterAuth(requestRegister: RequestRegister) {
        _isLoadingActive.value = true
        val api = StoryApiConfig.getApiStoriesConfig().createUserStories(requestRegister)
        api.enqueue(object : retrofit2.Callback<ResponseMessage> {
            override fun onResponse(
                call: Call<ResponseMessage>,
                response: Response<ResponseMessage>
            ) {
                _isLoadingActive.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    isErrorMessage = false
                    _messages.value = responseBody?.message.toString()
                } else {
                    isErrorMessage = true
                    _messages.value = response.message() }
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                isErrorMessage = true
                _isLoadingActive.value = false
            }

        })
    }
}