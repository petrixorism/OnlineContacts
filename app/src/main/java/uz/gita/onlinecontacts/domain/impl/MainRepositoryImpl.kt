package uz.gita.onlinecontacts.domain.impl

import uz.gita.onlinecontacts.data.local.SharedPref
import uz.gita.onlinecontacts.domain.repository.MainRepository

class MainRepositoryImpl : MainRepository {

    override suspend fun loggedIn(): Boolean {
        return SharedPref.getInstance().isLogedIn
    }

}