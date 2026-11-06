package com.example.medicaremobile.network

import com.example.medicaremobile.model.Patient
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    // Requête mtaâ Inscription (Register) men ghir Token
    @POST("api/patients/register") // Baddal el route hedhi 7asb chnowa 3amel el binôme fel Spring Boot
    suspend fun registerPatientWithoutToken(
        @Body patient: Patient
    ): Response<Patient>

    // Requête mtaâ Save b Token (Keycloak)
    @POST("api/patients")
    suspend fun savePatient(
        @Header("Authorization") token: String,
        @Body patient: Patient
    ): Response<Patient>
}