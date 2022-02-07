package uz.gita.onlinecontacts.domain.repository

interface MainRepository {

    suspend fun loggedIn(): Boolean

}