package com.example.storyapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class LoginResponse (
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult
) : Parcelable