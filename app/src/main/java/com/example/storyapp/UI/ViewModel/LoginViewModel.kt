package com.example.storyapp.UI.ViewModel

import androidx.lifecycle.*
import com.example.storyapp.API.StoryApiConfig
import com.example.storyapp.Model.LoginRequest
import com.example.storyapp.Model.LoginResponse
import retrofit2.Call
import retrofit2.Response

class LoginViewModel: ViewModel() {
    private var _messages = MutableLiveData<String>()
    var messages: LiveData<String> = _messages
    private var _isLoadingActive = MutableLiveData<Boolean>()
    var isLoadingActive: LiveData<Boolean> = _isLoadingActive
    private var _userLoginAuth = MutableLiveData<LoginResponse>()
    var userLoginAuth: LiveData<LoginResponse> = _userLoginAuth
    var isErrorMesssage: Boolean = false

    fun getUserResponseLoginAuth(requestLogin: LoginRequest) {
        _isLoadingActive.value = true
        val api = StoryApiConfig.getApiStoriesConfig().loginUserStories(requestLogin)
        api.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoadingActive.value = false
                val responseBody = response.body()

                if (response.isSuccessful) {
                    isErrorMesssage = false
                    _userLoginAuth.value = responseBody!!
                    _messages.value = "Login as ${_userLoginAuth.value!!.loginResult.name}"
                } else {
                    isErrorMesssage = true
                    _messages.value = response.message()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, throwable: Throwable) {
                isErrorMesssage = true
                _isLoadingActive.value = false
                _messages.value = throwable.message.toString()
            }

        })
    }
}