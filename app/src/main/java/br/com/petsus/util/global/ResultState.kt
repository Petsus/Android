package br.com.petsus.util.global

import br.com.petsus.util.base.viewmodel.StringFormatter

sealed class ResultState<T> {
    class Success<T>(val data: T) : ResultState<T>()
    class Fail<T>(val error: StringFormatter) : ResultState<T>()
    class Init<T> : ResultState<T>()
}