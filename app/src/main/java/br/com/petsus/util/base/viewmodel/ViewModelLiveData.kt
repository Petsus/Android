package br.com.petsus.util.base.viewmodel

import androidx.lifecycle.*
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

    fun <T> Call<T>.toLiveDataWithResponse(): LiveData<Response<T>> = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        runCatching {
            this@toLiveDataWithResponse.execute()
        }.onSuccess { response: Response<T> ->
            emit(response)
        }.callOnError()
    }

}