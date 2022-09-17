package com.rosen.convata.utils

/**
 * Resource state holder
 *
 * @param T The data to be saved in state
 * @property status The status of the Resource
 * @property data The data held in the state
 * @property message The error message to be displayed
 */
data class Resource<out T> constructor(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String): Resource<T> {
            return Resource(Status.ERROR, null, message)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }

    enum class Status {
        LOADING, SUCCESS, ERROR
    }
}