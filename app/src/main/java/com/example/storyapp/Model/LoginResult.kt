package com.example.storyapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResult(
    val userId: String,
    val name: String,
    val token: String,
): Parcelable
