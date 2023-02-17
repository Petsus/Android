package br.com.petsus.application.preferences

interface AppPreferences {

    fun putObject(key: String, value: Any?)

    fun <T> getObject(key: String, clazz: Class<T>): T?

}