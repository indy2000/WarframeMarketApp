package com.fukajima.warframerepo.entity

open class ResponseGeneric  {
    var success: Boolean = false
    var message: String? = null
    var exception: Throwable? = null
    var requestCode: Int? = null
}

class Response<T>: ResponseGeneric()  {
    var obj: T? = null

}

