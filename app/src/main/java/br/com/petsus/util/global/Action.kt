package br.com.petsus.util.global

fun interface Action<T> {
    fun action(data: T)
}

fun interface ActionSuspend<T> {
    suspend fun action(data: T)
}