package br.com.petsus.util.global

fun interface EmptyAction {
    fun action()
}

fun interface Action<T> {
    fun action(data: T)
}

fun interface Action2<T, D> {
    fun action(param1: T, param2: T)
}