package com.example.storyapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseStory(
    val error: String,
    val message: String,
    val listStory: List<ListStory>
): Parcelable