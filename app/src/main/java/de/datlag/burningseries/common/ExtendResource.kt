package de.datlag.burningseries.common

import com.hadiyarajesh.flower_core.Resource
import de.datlag.burningseries.R

val <T> Resource<T>.data: T?
    get() {
        return when (this.status) {
            is Resource.Status.SUCCESS -> (this.status as Resource.Status.SUCCESS).data
            is Resource.Status.LOADING -> (this.status as Resource.Status.LOADING).data
            is Resource.Status.ERROR -> (this.status as Resource.Status.ERROR).data
            else -> null
        }
    }

fun <T> Resource.Status.ERROR<T>.mapToMessageAndDisplayAction(): Pair<Int, Boolean> = when {
    this.statusCode < 100 -> R.string.error_internet_connecton to false
    this.statusCode in 400..499 -> R.string.error_bad_request to true
    this.statusCode in 500..599 -> R.string.error_server_error to true
    else -> R.string.error_could_not_load to true
}