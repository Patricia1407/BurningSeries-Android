package de.datlag.model.m3o.db

import android.os.Parcelable
import androidx.annotation.Keep
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Obfuscate
@Keep
data class Count(
    @SerialName("count") val count: Long = 0L
) : Parcelable
