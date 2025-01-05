package com.fukajima.warframerepo.entity

class Response<T>  {
    var obj: T? = null
    var success: Boolean = false
    var message: String? = null
    var exception: Throwable? = null
}