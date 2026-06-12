package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PreferencesState(
    val login: String = "devtitans",
    val password: String = "123",
    val preencher: Boolean = true
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    var preferencesState by mutableStateOf(
        PreferencesState(
            login = handle["login"] ?: "devtitans",
            password = handle["password"] ?: "123",
            preencher = handle["preencher"] ?: true
        )
    )
        private set

    fun updateLogin(login: String) {
        preferencesState = preferencesState.copy(login = login)
        handle["login"] = login
    }

    fun updatePassword(password: String) {
        preferencesState = preferencesState.copy(password = password)
        handle["password"] = password
    }

    fun updatePreencher(preencher: Boolean) {
        preferencesState = preferencesState.copy(preencher = preencher)
        handle["preencher"] = preencher
    }

    fun checkCredentials(login: String, password: String): Boolean {
        return login == preferencesState.login &&
                password == preferencesState.password
    }
}