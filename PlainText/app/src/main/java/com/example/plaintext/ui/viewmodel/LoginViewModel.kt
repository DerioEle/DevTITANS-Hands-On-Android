package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// REQUISITO 1: Data class representando o estado da view (textos, checkbox e botão)
data class LoginUiState(
    val loginText: String = "",
    val passwordText: String = "",
    val isSaveCredentialsChecked: Boolean = false,
    val isButtonEnabled: Boolean = false // Estado para o botão
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    // Mantém o estado da tela (leitura pública, alteração privada)
    var uiState by mutableStateOf(LoginUiState())
        private set

    // REQUISITO 2: Funções na viewModel que alteram os estados
    fun onLoginChanged(newLogin: String) {
        uiState = uiState.copy(
            loginText = newLogin,
            // O botão só ativa se login e senha não estiverem vazios
            isButtonEnabled = newLogin.isNotBlank() && uiState.passwordText.isNotBlank()
        )
    }

    fun onPasswordChanged(newPassword: String) {
        uiState = uiState.copy(
            passwordText = newPassword,
            isButtonEnabled = uiState.loginText.isNotBlank() && newPassword.isNotBlank()
        )
    }

    fun onSaveCredentialsChanged(checked: Boolean) {
        uiState = uiState.copy(isSaveCredentialsChecked = checked)
    }
}