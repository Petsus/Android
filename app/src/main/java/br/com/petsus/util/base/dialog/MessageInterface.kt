package br.com.petsus.util.base.dialog

import br.com.petsus.util.base.viewmodel.StringFormatter

interface ErrorInterface {
    fun error(message: StringFormatter): ErrorInterface
    fun closeError()
    fun onErrorClose() = Unit
}

interface SuccessInterface {
    fun message(message: StringFormatter): SuccessInterface
    fun closeMessage()
}

interface LoadingInterface {
    fun loading(): LoadingInterface
    fun closeLoading()
}