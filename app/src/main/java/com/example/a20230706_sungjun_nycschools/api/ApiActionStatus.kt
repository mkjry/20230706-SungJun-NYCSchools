package com.example.a20230706_sungjun_nycschools.api

enum class ApiActionStatus {
    IDLE_INITIALIZED,
    GETTING_SCHOOLS,
    SUCCESS_GET_SCHOOLS,
    FAIL_GET_SCHOOLS,
    GETTING_SATS,
    SUCCESS_GET_SATS,
    FAIL_GET_SATS
}