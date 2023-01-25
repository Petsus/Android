package br.com.petsus.normal.api.exception

import br.com.petsus.R
import br.com.petsus.api.exception.RepositoryException
import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.api.model.base.ErrorResponse
import br.com.petsus.util.base.viewmodel.StringFormatter
import com.google.gson.Gson
import kotlinx.coroutines.flow.FlowCollector
import okhttp3.ResponseBody
import retrofit2.Response

class ApiException(
    private val response: Response<*>
) : RepositoryException("Invalid response, status code: ${response.code()}") {

    override fun formatter(): StringFormatter {
        try {
            return response.errorBody().evaluateError()
        } catch (_: Throwable) {
        }

        return StringFormatter()
    }

    private fun ResponseBody?.evaluateError(): StringFormatter {
        var defaultResponse = StringFormatter(messageId = R.string.error_generic)
        runCatching {
            if (this == null)
                return@runCatching
            val fromJson = Gson().fromJson(this.string(), ErrorResponse::class.java)
            defaultResponse = defaultResponse.copy(messageString = fromJson.message)
        }

        return defaultResponse
    }
}

@Suppress("UNCHECKED_CAST")
suspend fun <T>FlowCollector<T>.send(response: Response<*>) {
    if (response.isSuccessful) {
        when (val value = response.body()) {
            is BaseResponse<*> -> emit(value.data as T)
            else -> emit(value as T)
        }
        return
    }

    throw ApiException(response)
}
