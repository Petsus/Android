package br.com.petsus.util.tool

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Resources
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveDataScope
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.global.ActionSuspend
import br.com.petsus.util.global.ResultState
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlin.math.roundToInt

val Location.latLng: LatLng
    get() = LatLng(latitude, longitude)

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

suspend fun <T> Flow<T>.collector(
    liveDataScope: LiveDataScope<T>,
    onCollect: ActionSuspend<T>? = null
) {
    onStart { Log.i("Flow", "Start loading") }
        .catch { Log.i("Flow", "Error flow"); it.printStackTrace() }
        .collect { value ->
            liveDataScope.emit(value)
            onCollect?.action(value)
        }
}

suspend fun <T> Flow<T>.collectorState(
    stateFlow: MutableStateFlow<ResultState<T>>,
) {
    onStart {
        Log.i("Flow", "Start loading")
        stateFlow.value = ResultState.Init()
    }.catch {
        it.printStackTrace()
        Log.i("Flow", "Error flow")
        stateFlow.value = ResultState.Fail(error = StringFormatter(throwable = it))
    }.collect { value ->
        stateFlow.value = ResultState.Success(data = value)
    }
}