package br.com.petsus.util.tool

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Patterns
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LiveDataScope
import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.BuildConfig
import br.com.petsus.api.exception.RepositoryException
import br.com.petsus.util.base.viewmodel.AppViewModelInterface
import br.com.petsus.util.base.viewmodel.MessageThrowable
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.base.viewmodel.printStackTrace
import br.com.petsus.util.global.ActionSuspend
import br.com.petsus.util.global.ResultState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToInt

fun String.toDate(pattern: String = "yyyy-MM-dd hh:mm:ss"): Date? {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return runCatching { format.parse(this) }.getOrNull()
}

fun Date.format(pattern: String = "dd/MM/yyyy"): String {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(this)
}

val Location.latLng: LatLng
    get() = LatLng(latitude, longitude)

val View.inflater: LayoutInflater
    get() = LayoutInflater.from(this.context)

@Suppress("unchecked_cast")
fun <T> Any.cast() = this as T

@Suppress("unchecked_cast")
fun <T> Any.tryCast() = this as? T

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

val String?.isName: Boolean
    get() = Pattern.compile("^[a-záàâãéèêíïóôõöúçñ ]+\$").matcher(this?.lowercase() ?: "").matches()

val String?.isEmail: Boolean
    get() = Patterns.EMAIL_ADDRESS.matcher(this?.lowercase() ?: "").matches()

val String?.isPhone: Boolean
    get() = Pattern.compile("^[0123456789]+\$").matcher((this?.lowercase() ?: "").replace("[^0-9]".toRegex(), "")).matches()

val String?.digits: String?
    get() = this?.run { replace("[^0-9]".toRegex(), "") }

val RecyclerView.ViewHolder.context: Context
    get() = itemView.context

fun View.preventDoubleClick() {
    isClickable = false
    Handler(Looper.getMainLooper()).postDelayed({ isClickable = true }, 1000)
}

val TextInputLayout.text: String?
    get() = editText?.text?.toString()

@Suppress("DEPRECATION")
fun <T : Parcelable> Intent.parcelable(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getParcelableExtra(key, clazz)
    else getParcelableExtra(key)
}

@Suppress("DEPRECATION")
fun <T : Parcelable> Bundle.parcelable(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getParcelable(key, clazz)
    else getParcelable(key)
}

suspend fun <T> Flow<T>.collector(
    liveDataScope: LiveDataScope<T>? = null,
    onCollect: ActionSuspend<T>? = null,
    viewModel: AppViewModelInterface? = null
) {
    baseFlow(viewModel = viewModel).collect { value ->
        liveDataScope?.emit(value)
        onCollect?.action(value)
    }
}

fun <T> Flow<T>.baseFlow(
    viewModel: AppViewModelInterface? = null
): Flow<T> {
    return catch {
        val error = it.messageString
        if (BuildConfig.DEBUG)
            error.printStackTrace()
        viewModel?.notify(error)
    }
}

suspend fun <T> Flow<T>.collectorState(
    stateFlow: MutableStateFlow<ResultState<T>>,
) {
    onStart {
        stateFlow.value = ResultState.Init()
    }.catch {
        stateFlow.value = ResultState.Fail(error = StringFormatter(throwable = it))
    }.collect { value ->
        stateFlow.value = ResultState.Success(data = value)
    }
}

fun GoogleMap.configure() {
    uiSettings.isCompassEnabled = false
    uiSettings.isMapToolbarEnabled = false
}

//"$address - $neighborhood, ${city.name}/${city.state.initials}"
val Address.completeAddress: String
    get() {
        var address = ""
        this.thoroughfare?.apply { address += this }
        this.subThoroughfare?.apply { address += " - $this" }
        this.subAdminArea?.apply { address += ", $this" }
        this.countryName?.apply { address += "/$this" }

        return address
    }

val Address.location: LatLng
    get() {
        if (!hasLatitude() || !hasLongitude())
            return LatLng(0.0, 0.0)
        return LatLng(latitude, longitude)
    }

val Throwable.messageString: StringFormatter
    get() = when (this) {
        is MessageThrowable -> this.appMessage
        is RepositoryException -> this.formatter()
        else -> StringFormatter(throwable = this)
    }

@Suppress("DEPRECATION")
suspend fun Context.addressFromLocation(latLng: LatLng): Address? {
    return suspendCoroutine { continuation ->
        runCatching {
            val geocoder = Geocoder(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { addresses ->
                    addresses.firstOrNull()?.run(continuation::resume)
                }
                return@runCatching
            }
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.firstOrNull() ?: throw Throwable()
            continuation.resume(address)
        }.onFailure { continuation.resumeWithException(it) }
    }
}
inline fun <T> T.tryCatch(
    viewModel: AppViewModelInterface?,
    block: T.() -> Unit,
    completion: () -> Unit
) {
    try {
        block()
        completion()
    } catch (e: MessageThrowable) {
        viewModel?.notify(e.messageString)
    }
}