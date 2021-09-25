package com.dicoding.githubusersapp.common

class DataState<out E>(
    val state:State,
    val data:E?,
    val message:String?
) {
    companion object {
        fun <E> success(data: E): DataState<E> = DataState(State.SUCCESS, data, null)

        fun <E> error(data: E?, message: String): DataState<E> = DataState(State.ERROR, data, message)

        fun <E> loading(data: E?, message: String): DataState<E> = DataState(State.LOADING, data, message)
    }
}