package uz.gita.onlinecontacts.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.activemedia.udic.contactapp.utils.isConnected
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.local.ResultData
import uz.gita.onlinecontacts.data.response.AuthResponse
import uz.gita.onlinecontacts.domain.impl.AuthrepositoryImpl
import uz.gita.onlinecontacts.domain.repository.AuthRepository

class LoginViewModel : ViewModel() {

    private val repository: AuthRepository = AuthrepositoryImpl()

    val progressLiveData = MediatorLiveData<Boolean>()
    val loginLiveData = MutableLiveData<AuthResponse>()
    val failLiveData = MutableLiveData<String>()
    val notConnectionLiveData = MutableLiveData<String>()

    fun login(data: NamePasswordData) {

        if (!isConnected()) {
            notConnectionLiveData.value = "Internet mavjud emas!"
        }

        viewModelScope.launch {
            progressLiveData.postValue(true)
            progressLiveData.addSource(repository.login(data)) {
                when (it) {
                    is ResultData.Success -> loginLiveData.postValue(it.data!!)
                    is ResultData.Message -> {
                        Log.d("TTT", it.message)
                        failLiveData.postValue(it.message)
                    }
                    is ResultData.Error -> {
                        Log.d("TTT", it.error.toString())
                        failLiveData.postValue(it.error.toString())
                    }
                }
                progressLiveData.value = false
            }

        }

    }

}