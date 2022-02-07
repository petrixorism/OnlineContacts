package uz.gita.onlinecontacts.ui.viewmodel

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

class RegisterViewModel : ViewModel() {

    private val repository: AuthRepository = AuthrepositoryImpl()

    val progressLiveData = MediatorLiveData<Boolean>()
    val registerLiveData = MutableLiveData<AuthResponse>()
    val failLiveData = MutableLiveData<String>()
    val notConnectionLiveData = MutableLiveData<String>()

    fun register(data: NamePasswordData) {

        if (!isConnected()) {
            notConnectionLiveData.value = "Internet mavjud emas!"
        }

        viewModelScope.launch {
            progressLiveData.postValue(true)
            progressLiveData.addSource(repository.register(data)) {
                when (it) {
                    is ResultData.Success -> registerLiveData.postValue(it.data!!)
                    is ResultData.Message -> {
                        failLiveData.postValue(it.message)
                    }
                    is ResultData.Error -> {
                        failLiveData.postValue(it.error.toString())
                    }
                }
                progressLiveData.value = false
            }

        }

    }

}