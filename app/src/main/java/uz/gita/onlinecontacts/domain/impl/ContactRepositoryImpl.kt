package uz.gita.onlinecontacts.domain.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import uz.gita.onlinecontacts.data.Network
import uz.gita.onlinecontacts.data.api.ContactApi
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.auth_request.NamePhoneData
import uz.gita.onlinecontacts.data.local.ResultData
import uz.gita.onlinecontacts.data.local.SharedPref
import uz.gita.onlinecontacts.data.response.ContactResponse
import uz.gita.onlinecontacts.domain.repository.ContactRepository

class ContactRepositoryImpl : ContactRepository {

    private val api = Network.retrofit.create(ContactApi::class.java)
    private val pref = SharedPref.getInstance()


    override suspend fun getAllContacts(): LiveData<ResultData<List<ContactResponse>>> = liveData {

        try {
            val response = api.getContcatList()

            if (response.isSuccessful) {
                response.body().apply {
                    emit(ResultData.Success<List<ContactResponse>>(this!!.data!!))
                }
            } else {
                emit(ResultData.Message<List<ContactResponse>>(response.message()))
            }

        } catch (e: Throwable) {
            emit(ResultData.Error<List<ContactResponse>>(e))
        }

    }

    override suspend fun addContact(data: NamePhoneData): LiveData<ResultData<ContactResponse>> =
        liveData {

            try {
                val response = api.addContact(
                    data
                )

                if (response.isSuccessful) {
                    response.body().apply {
                        emit(ResultData.Success<ContactResponse>(this!!.data!!))
                    }
                } else {
                    emit(ResultData.Message<ContactResponse>(response.message()))
                }

            } catch (e: Throwable) {
                emit(ResultData.Error<ContactResponse>(e))
            }
        }

    override suspend fun editContact(data: ContactResponse): LiveData<ResultData<ContactResponse>> =
        liveData {

            try {
                val response = api.editContact(data)

                if (response.isSuccessful) {
                    response.body().apply {
                        emit(ResultData.Success<ContactResponse>(this!!.data!!))
                    }
                } else {
                    emit(ResultData.Message<ContactResponse>(response.message()))
                }

            } catch (e: Throwable) {
                emit(ResultData.Error<ContactResponse>(e))
            }
        }

    override suspend fun deleteContact(id: Int): LiveData<ResultData<ContactResponse>> =
        liveData {

            try {
                val response = api.deleteContact(id)

                if (response.isSuccessful) {
                    response.body().apply {
                        emit(ResultData.Success<ContactResponse>(this!!.data!!))
                    }
                } else {
                    emit(ResultData.Message<ContactResponse>(response.message()))
                }

            } catch (e: Throwable) {
                emit(ResultData.Error<ContactResponse>(e))
            }
        }


}