package uz.gita.onlinecontacts.data.response

data class MainResponse<T>(
    val data: T? = null,
    val message: String? = null
)
