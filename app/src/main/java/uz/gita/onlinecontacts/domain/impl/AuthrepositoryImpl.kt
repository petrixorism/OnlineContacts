package uz.gita.onlinecontacts.domain.impl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import uz.gita.onlinecontacts.data.Network
import uz.gita.onlinecontacts.data.api.AuthApi
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.local.ResultData
import uz.gita.onlinecontacts.data.local.SharedPref
import uz.gita.onlinecontacts.data.response.AuthResponse
import uz.gita.onlinecontacts.domain.repository.AuthRepository

class AuthrepositoryImpl : AuthRepository {

    private val api = Network.retrofit.create(AuthApi::class.java)
    private val pref = SharedPref.getInstance()

    override fun register(data: NamePasswordData): LiveData<ResultData<AuthResponse>> = liveData {

        try {
            val response = api.register(data)

            if (response.isSuccessful) {
                response.body().apply {
                    pref.password = data.password
                    pref.isLogedIn = true
                    pref.name = this!!.data!!.name
                    pref.token = this.data!!.token

                    emit(ResultData.Success<AuthResponse>(this!!.data!!))
                }
            } else {
                Log.d("TTT", response.message().toString())

                emit(ResultData.Message<AuthResponse>(response.message().toString()))
            }
        } catch (e: Throwable) {
            emit(ResultData.Error<AuthResponse>(e))
        }

    }

    override fun login(data: NamePasswordData): LiveData<ResultData<AuthResponse>> = liveData {

        try {
            val response = api.login(data)
            if (response.isSuccessful) {

                response.body()!!.data.apply {
                    pref.password = data.password
                    pref.isLogedIn = true
                    pref.name = this!!.name
                    pref.token = this.token

                    emit(ResultData.Success<AuthResponse>(this!!))
                }

            } else {
                emit(ResultData.Message<AuthResponse>(response.message()))
            }

        } catch (e: Throwable) {
            emit(ResultData.Error<AuthResponse>(e))
        }

    }

    override fun unregister(): LiveData<ResultData<NamePasswordData>> = liveData {


        try {
            val response = api.unregister( NamePasswordData(
                pref.name,
                pref.password
            ))
            if (response.isSuccessful) {

                response.body()!!.data.apply {
                    pref.password = ""
                    pref.isLogedIn = false
                    pref.name = ""
                    pref.token = ""
                    emit(ResultData.Success<NamePasswordData>(this!!))
                }

            } else {
                emit(ResultData.Message<NamePasswordData>(response.message()))
            }

        } catch (e: Throwable) {
            emit(ResultData.Error<NamePasswordData>(e))
        }

    }

    override fun logout(): LiveData<ResultData<Unit>> = liveData {
        try {
            val response = api.logout( NamePasswordData(
                pref.name,
                pref.password
            ))
            if (response.isSuccessful) {
                response.body().apply {
                    pref.password = ""
                    pref.isLogedIn = false
                    pref.name = ""
                    pref.token = ""

                    emit(ResultData.Success<Unit>(Unit))
                }

            } else {
                emit(ResultData.Message<Unit>(response.message()))
            }

        } catch (e: Throwable) {
            emit(ResultData.Error<Unit>(e))
        }
    }

}