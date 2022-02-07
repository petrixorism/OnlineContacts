package uz.gita.onlinecontacts.domain.repository

import androidx.lifecycle.LiveData
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.auth_request.NamePhoneData
import uz.gita.onlinecontacts.data.local.ResultData
import uz.gita.onlinecontacts.data.response.ContactResponse

interface ContactRepository {

    suspend fun getAllContacts(): LiveData<ResultData<List<ContactResponse>>>

    suspend fun addContact(data: NamePhoneData): LiveData<ResultData<ContactResponse>>

    suspend fun editContact(data: ContactResponse): LiveData<ResultData<ContactResponse>>

    suspend fun deleteContact(id: Int): LiveData<ResultData<ContactResponse>>
}