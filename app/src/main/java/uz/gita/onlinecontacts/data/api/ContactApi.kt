package uz.gita.onlinecontacts.data.api

import retrofit2.Response
import retrofit2.http.*
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.auth_request.NamePhoneData
import uz.gita.onlinecontacts.data.response.ContactResponse
import uz.gita.onlinecontacts.data.response.MainResponse

interface ContactApi {

    @GET("contact")
    suspend fun getContcatList(): Response<MainResponse<List<ContactResponse>>>

    @POST("contact")
    suspend fun addContact(@Body data: NamePhoneData): Response<MainResponse<ContactResponse>>

    @PUT("contact")
    suspend fun editContact(@Body data: ContactResponse): Response<MainResponse<ContactResponse>>

    @DELETE("contact")
    suspend fun deleteContact(@Query("id") id: Int): Response<MainResponse<ContactResponse>>


}