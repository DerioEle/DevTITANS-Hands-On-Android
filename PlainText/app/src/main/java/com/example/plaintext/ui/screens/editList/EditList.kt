package com.example.plaintext.ui.screens.editList

import com.example.plaintext.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.Screen


data class EditListState(
    val nomeState: MutableState<String>,
    val usuarioState: MutableState<String>,
    val senhaState: MutableState<String>,
    val notasState: MutableState<String>,
)

fun isPasswordEmpty(password: PasswordInfo): Boolean {
    return password.name.isEmpty() && password.login.isEmpty() && password.password.isEmpty() && password.notes.orEmpty().isEmpty()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditList(
    args: Screen.EditList,
    navigateBack: () -> Unit,
    savePassword: (password: PasswordInfo) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isPasswordEmpty(args.password)) "Adicionar nova senha" else "Editar senha")}
            )
        },
        content = { innerPadding ->
            EditListContent(innerPadding, args.password, savePassword, navigateBack)
        },
        containerColor = colorResource(R.color.dark_brown)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditListContent(
    scaffoldInnerPadding: PaddingValues,
    passwordToBeEdited: PasswordInfo,
    savePassword: (password: PasswordInfo) -> Unit,
    navigateBack: () -> Unit
) {
    // Initialize states with password data
    val nomeState = rememberSaveable { mutableStateOf(passwordToBeEdited.name) }
    val usuarioState = rememberSaveable { mutableStateOf(passwordToBeEdited.login) }
    val senhaState = rememberSaveable { mutableStateOf(passwordToBeEdited.password) }
    val notasState = rememberSaveable { mutableStateOf(passwordToBeEdited.notes ?: "") }

    Column(
        modifier = Modifier
            .padding(scaffoldInnerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        EditInput(stringResource(R.string.input_name), nomeState)
        EditInput(stringResource(R.string.input_user), usuarioState)
        EditInput(stringResource(R.string.input_password), senhaState)
        EditInput(stringResource(R.string.input_notes), notasState, 150)

        Button(onClick = {
            val updatedPassword = passwordToBeEdited.copy(
                name = nomeState.value,
                login = usuarioState.value,
                password = senhaState.value,
                notes = notasState.value
            )
            savePassword(updatedPassword)
            navigateBack()
        }) {
            Text(text = "Salvar")
        }
    }
}

@Composable
fun EditInput(
    textInputLabel: String,
    textInputState: MutableState<String> = mutableStateOf(""),
    textInputHeight: Int = 60
) {
    val padding: Int = 30

    var textState by rememberSaveable { textInputState }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(textInputHeight.dp)
            .padding(horizontal = padding.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            value = textState,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = colorResource(R.color.green_android),
                unfocusedBorderColor = Color.Gray
            ),
            onValueChange = { textState = it },
            label = { Text(textInputLabel) },
            modifier = Modifier
                .height(textInputHeight.dp)
                .fillMaxWidth()
        )

    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Preview(showBackground = true)
@Composable
fun EditListPreview() {
    EditList(
        Screen.EditList(PasswordInfo(1, "Nome", "Usuário", "Senha", "Notas")),
        navigateBack = {},
        savePassword = {}
    )
}

@Preview(showBackground = true)
@Composable
fun AddListPreview() {
    EditList(
        Screen.EditList(PasswordInfo(1, "", "", "", "")),
        navigateBack = {},
        savePassword = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EditInputPreview() {
    EditInput("Nome")
}