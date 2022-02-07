package uz.gita.onlinecontacts.ui.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.activemedia.udic.contactapp.utils.isConnected
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.auth_request.NamePhoneData
import uz.gita.onlinecontacts.data.local.ResultData
import uz.gita.onlinecontacts.data.response.ContactResponse
import uz.gita.onlinecontacts.domain.impl.AuthrepositoryImpl
import uz.gita.onlinecontacts.domain.impl.ContactRepositoryImpl
import uz.gita.onlinecontacts.domain.repository.AuthRepository
import uz.gita.onlinecontacts.domain.repository.ContactRepository

class HomeViewModel : ViewModel() {

    private val repositoryContact: ContactRepository = ContactRepositoryImpl()
    private val repositoryAuth: AuthRepository = AuthrepositoryImpl()


    val progressLiveData = MediatorLiveData<Boolean>()
    val getAllContactLiveData = MutableLiveData<List<ContactResponse>>()
    val notifyChangeLiveData = MutableLiveData<Unit>()
    val userExitLiveData = MutableLiveData<Unit>()
    val failLiveData = MutableLiveData<String>()
    val notConnectionLiveData = MutableLiveData<String>()


    fun getContactList() {

        if (!isConnected()) {
            notConnectionLiveData.value = "Internet mavjud emas!"
        }

        viewModelScope.launch {
            progressLiveData.addSource(repositoryContact.getAllContacts()) {

                when (it) {
                    is ResultData.Success -> getAllContactLiveData.postValue(it.data!!)
                    is ResultData.Message -> {
                        failLiveData.postValue(it.message)
                    }
                    is ResultData.Error -> {
                        failLiveData.postValue(it.error.toString())
                    }
                }

            }

        }

    }

    fun addContact(data: NamePhoneData) {

        if (!isConnected()) {
            notConnectionLiveData.value = "Internet mavjud emas!"
        }
        else {
            viewModelScope.launch {
                progressLiveData.postValue(true)
                progressLiveData.addSource(repositoryContact.addContact(data)) {

                    when (it) {
                        is ResultData.Success -> notifyChangeLiveData.postValue(Unit)
                        is ResultData.Message -> failLiveData.postValue(it.message)
                        is ResultData.Error -> failLiveData.postValue(it.error.toString())
                    }
                    progressLiveData.value = false

                }

            }
        }
    }

    fun editContact(data: ContactResponse) {

        if (!isConnected()) {
            notConnectionLiveData.value = "Internet mavjud emas!"
        } else {
            viewModelScope.launch {
                progressLiveData.postValue(true)
                progressLiveData.addSource(repositoryContact.editContact(data)) {

                    when (it) {
                        is ResultData.Success -> notifyChangeLiveData.postValue(Unit)
                        is ResultData.Message -> failLiveData.postValue(it.message)
                        is ResultData.Error -> failLiveData.postValue(it.error.toString())
                    }
                    progressLiveData.value = false
                }
            }

        }

    }

    fun deleteContact(id: Int) {

        if (!isConnected()) {
            notConnectionLiveData.value = "Internet mavjud emas!"
        }

        viewModelScope.launch {
            progressLiveData.postValue(true)
            progressLiveData.addSource(repositoryContact.deleteContact(id)) {

                when (it) {
                    is ResultData.Success -> notifyChangeLiveData.postValue(Unit)
                    is ResultData.Message -> failLiveData.postValue(it.message)
                    is ResultData.Error -> failLiveData.postValue(it.error.toString())
                }
                progressLiveData.value = false

            }

        }

    }

    fun logout() {
        if (!isConnected()) {
            notConnectionLiveData.value = "Internet mavjud emas!"
        }

        viewModelScope.launch {
            progressLiveData.postValue(true)
            progressLiveData.addSource(repositoryAuth.logout()) {
                when (it) {
                    is ResultData.Success -> userExitLiveData.postValue(Unit)
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

    fun deleteAccount() {
        if (!isConnected()) {
            notConnectionLiveData.value = "Internet mavjud emas!"
        }

        viewModelScope.launch {
            progressLiveData.postValue(true)
            progressLiveData.addSource(repositoryAuth.unregister()) {
                when (it) {
                    is ResultData.Success -> userExitLiveData.postValue(Unit)
                    is ResultData.Message -> failLiveData.postValue(it.message)
                    is ResultData.Error -> failLiveData.postValue(it.error.toString())

                }
                progressLiveData.value = false
            }

        }

    }


}