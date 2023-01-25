package br.com.petsus.util.tool

import android.util.Log
import br.com.petsus.BuildConfig

fun log(message: Any) {
    if (!BuildConfig.DEBUG)
        return
    Log.println(Log.DEBUG, "DEFAULT_TAG", message.toString())
}

fun log(tag: String, message: Any) {
    if (!BuildConfig.DEBUG)
        return
    Log.println(Log.DEBUG, tag, message.toString())
}