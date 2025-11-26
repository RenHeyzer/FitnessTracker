package com.azim.fitness.utils

sealed class UIState<out T, out E> {
    data object Loading: UIState<Nothing, Nothing>()
    class Error<E>(val error: E): UIState<Nothing, E>()
    class Success<T>(val data: T): UIState<T, Nothing>()
}