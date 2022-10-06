package com.example.storyapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestRegister (
    val name: String,
    val email: String,
    val password: String,
): Parcelable