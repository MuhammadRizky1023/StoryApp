package com.example.storyapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseMessage(
    val error: Boolean,
    val message: String
): Parcelable