package uz.gita.onlinecontacts.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.response.AuthResponse
import uz.gita.onlinecontacts.data.response.MainResponse

interface AuthApi {

    @POST("register")
    suspend fun register(@Body data: NamePasswordData): Response<MainResponse<AuthResponse>>

    @POST("login")
    suspend fun login(@Body data: NamePasswordData): Response<MainResponse<AuthResponse>>

    @POST("unregister")
    suspend fun unregister(@Body data: NamePasswordData): Response<MainResponse<NamePasswordData>>

    @POST("logout")
    suspend fun logout(@Body data: NamePasswordData): Response<MainResponse<Nothing>>

}