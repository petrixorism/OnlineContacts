package uz.gita.onlinecontacts.data.local

sealed class ResultData<T> {

    class Success<T>(val data: T) : ResultData<T>()
    class Message<T>(val message: String) : ResultData<T>()
    class Error<T>(val error: Throwable) : ResultData<T>()

}
