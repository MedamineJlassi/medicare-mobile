package com.example.medicaremobile.model

data class Patient(
    val id: Long? = null,
    val nom: String,
    val prenom: String,
    val email: String,
    val telephone: String
)