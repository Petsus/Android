package br.com.petsus.api.exception

import br.com.petsus.util.base.viewmodel.StringFormatter

abstract class RepositoryException : Throwable {
    constructor() : super()
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?) : super(message)

    abstract fun formatter(): StringFormatter
}