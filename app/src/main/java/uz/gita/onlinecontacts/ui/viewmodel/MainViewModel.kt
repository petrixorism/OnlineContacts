package uz.gita.onlinecontacts.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.gita.onlinecontacts.domain.impl.MainRepositoryImpl
import uz.gita.onlinecontacts.domain.repository.MainRepository

class MainViewModel : ViewModel() {

    private val repository: MainRepository = MainRepositoryImpl()

    val loggedLiveData = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            loggedLiveData.postValue(repository.loggedIn())
        }
    }
}