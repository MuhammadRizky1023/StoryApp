package com.example.storyapp.UI.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.Preference.StoryUserPreference
import kotlinx.coroutines.launch

class StoryUserViewModel(private  val pref:  StoryUserPreference): ViewModel() {
    fun getUserLoginAuth(): LiveData<Boolean> {
        return pref.getLoginAuth().asLiveData()
    }

    fun saveUserLoginAuth(isLogin: Boolean) {
        viewModelScope.launch {
            pref.savingLoginAuth(isLogin)
        }
    }


    fun getTokenAuth(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveTokenAuth(token: String) {
        viewModelScope.launch {
            pref.savingTokenAuth(token)
        }
    }

    fun getUserName(): LiveData<String> {
        return pref.getUser().asLiveData()
    }

    fun saveUserName(name: String) {
        viewModelScope.launch {
            pref.saveUser(name)
        }
    }
}