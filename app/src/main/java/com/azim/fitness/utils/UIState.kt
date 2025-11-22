package com.azim.fitness.utils

sealed class UIState<out T> {
    data object Loading: UIState<Nothing>()
    class Error(val error: Throwable): UIState<Nothing>()
    class Success<T>(val data: T): UIState<T>()
}