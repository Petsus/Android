package br.com.petsus.util.base.viewmodel

import android.content.Context
import br.com.petsus.R

data class StringFormatter(
    val messageId: Int? = null,
    val messageString: String? = null,
    val throwable: Throwable? = null,
)

class MessageThrowable(
    val appMessage: StringFormatter
) : Throwable()

fun StringFormatter.parse(context: Context): String {
    return messageString ?: context.getString(messageId ?: R.string.error_generic)
}

fun StringFormatter.printStackTrace() {
    throwable?.printStackTrace()
}