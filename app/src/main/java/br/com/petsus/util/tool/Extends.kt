package br.com.petsus.util.tool

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.lifecycle.LiveDataScope
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlin.math.roundToInt

val Context.sharedPreferences: SharedPreferences
    get() = getSharedPreferences(Keys.APP_PREFERENCES.valueKey, Context.MODE_PRIVATE)

fun SharedPreferences.putObject(key: String, value: Any?) = edit { putString(key, value?.json) }

inline fun <reified T> SharedPreferences.getObject(key: String): T? {
    val jsonValue = getString(key, null) ?: return null
    return Gson().fromJson(jsonValue, T::class.java)
}

val View.inflater: LayoutInflater
    get() = LayoutInflater.from(this.context)

@Suppress("unchecked_cast")
fun <T> Any.cast() = this as T

val Number.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

val Number.pixel: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

val Any.json: String
    get() = Gson().toJson(this)


val String?.isEmail: Boolean
    get() = Patterns.EMAIL_ADDRESS.matcher(this ?: "").matches()

fun View.preventDoubleClick() {
    isClickable = false
    Handler(Looper.getMainLooper()).postDelayed({ isClickable = true }, 1000)
}

val TextInputLayout.text: String?
    get() = editText?.text?.toString()

fun Toolbar.listenerDismiss(activity: Activity?) {
    setNavigationOnClickListener {
        activity?.finish()
    }
}

suspend fun <T>Flow<T>.collector(liveDataScope: LiveDataScope<T>) {
    onStart { Log.i("Flow", "Start loading") }
        .catch { Log.i("Flow", "Error flow"); it.printStackTrace() }
        .collect(liveDataScope::emit)
}