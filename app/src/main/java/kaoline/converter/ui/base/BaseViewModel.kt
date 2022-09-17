package kaoline.converter.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kaoline.converter.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    protected val _loadingState: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    protected val _errorEvent: SingleLiveEvent<Throwable> = SingleLiveEvent()
    val errorEvent: SingleLiveEvent<Throwable>
        get() = _errorEvent


    protected fun <T> collect(action: Flow<Result<T>>, onError: ((Result.Failure) -> Unit)? = null, onSuccess: (Result.Success<T>) -> Unit) {
        viewModelScope.launch {
            action.onEach {
                when (it) {
                    is Result.Loading -> _loadingState.postValue(true)
                    is Result.Failure -> {
                        _loadingState.postValue(false)
                        if (onError == null) _errorEvent.postValue(it.error)
                        else onError(it)
                    }
                    is Result.Success -> {
                        _loadingState.postValue(false)
                        onSuccess(it)
                    }
                }
            }.flowOn(Dispatchers.IO).collect()
        }
    }
}