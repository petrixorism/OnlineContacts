package uz.gita.onlinecontacts.domain.repository

import androidx.lifecycle.LiveData
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.local.ResultData
import uz.gita.onlinecontacts.data.response.AuthResponse

interface AuthRepository {

    fun register(data:NamePasswordData): LiveData<ResultData<AuthResponse>>

    fun login(data:NamePasswordData): LiveData<ResultData<AuthResponse>>

    fun unregister(): LiveData<ResultData<NamePasswordData>>

    fun logout(): LiveData<ResultData<Unit>>

}