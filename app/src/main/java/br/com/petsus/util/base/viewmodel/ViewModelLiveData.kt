package br.com.petsus.util.base.viewmodel

import androidx.lifecycle.*
import br.com.petsus.R
import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.util.tool.evaluateError
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Response

abstract class ViewModelLiveData : ViewModel() {

    private val errorObserver: MutableLiveData<StringFormatter> = MutableLiveData()

    val error: LiveData<StringFormatter>
        get() = errorObserver

    fun sendError(error: StringFormatter) = errorObserver.postValue(error)

    fun Result<*>.callOnError() {
        onFailure { error ->
            error.printStackTrace()
            errorObserver.postValue(StringFormatter(throwable = error))
        }
    }

    suspend fun <T> Result<T>.callOnSuccessful(liveDataScope: LiveDataScope<T>) {
        onSuccess { value ->
            liveDataScope.emit(value)
        }
    }

    suspend fun <T> Result<T>.emit(liveDataScope: LiveDataScope<T>) {
        callOnSuccessful(liveDataScope)
        callOnError()
    }

    fun <T> Call<BaseResponse<T>>.toLiveDataBaseResponse(): LiveData<T> = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        runCatching {
            this@toLiveDataBaseResponse.execute()
        }.onSuccess { response: Response<BaseResponse<T>> ->
            when {
                response.code() in 200..299 -> emit(response.body().data)
                else -> sendError(response.errorBody().evaluateError())
            }
        }.callOnError()
    }

    fun <T> Call<T>.toLiveData(): LiveData<T> = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        runCatching {
            this@toLiveData.execute()
        }.onSuccess { response: Response<T> ->
            when {
                response.code() in 200..299 -> emit(response.body())
                else -> sendError(response.errorBody().evaluateError())
            }
        }.callOnError()
    }

    fun <T> Call<T>.toLiveDataWithResponse(): LiveData<Response<T>> = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        runCatching {
            this@toLiveDataWithResponse.execute()
        }.onSuccess { response: Response<T> ->
            emit(response)
        }.callOnError()
    }

}