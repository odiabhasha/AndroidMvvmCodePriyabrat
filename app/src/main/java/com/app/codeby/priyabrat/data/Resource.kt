package com.app.codeby.priyabrat.data

sealed class Resource <out T> (data: T?, message: String) {
    data class Success<T>(var data: T) : Resource<T>(data,"")
    data class Error(var error: String): Resource<Nothing>(null, "")
    object Loading : Resource<Nothing>(null, "")
}

