package br.com.petsus.util.base.viewmodel

import android.content.Context
import br.com.petsus.R

data class StringFormatter(
    val messageId: Int? = null,
    val messageString: String? = null,
    val throwable: Throwable? = null,
)

fun StringFormatter.parse(context: Context): String {
    return messageString ?: context.getString(messageId ?: R.string.error_generic)
}

fun StringFormatter.printStackTrace() {
    throwable?.printStackTrace()
}

val StringFormatter.getResourceIDOrThrow: Int
    get() = messageId ?: throw NullPointerException("messageId is null")