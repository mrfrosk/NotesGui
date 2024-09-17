package Pages

import Pages.enums.GlobalPages
import UiComponents.robotoText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import data.source.SpringDataSource
import data.dto.UserInfoDto
import data.source.ISource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfilePage(private val navigateState: MutableState<GlobalPages>, val userState: MutableState<UserInfoDto?>) {
    private val source: ISource = SpringDataSource()

    @Composable
    fun draw() {
        val scope = rememberCoroutineScope()
        var expanded by remember { mutableStateOf(false) }
        val users by mutableStateOf(mutableListOf<UserInfoDto>())
        val currentUser = remember { mutableStateOf<String>("") }
        val readyToLogin = remember { mutableStateOf(false) }
        scope.launch {
            users.addAll(async { source.getUsers() }.await())
            if (currentUser.value == ""){
                currentUser.value = users[0].email
            }
        }

        Column {
            Column {
                TextButton(onClick = { expanded = !expanded }) {
                    robotoText("Текущий пользователь: ${currentUser.value}")
                }
                userDropDownMenu(expanded, {
                    expanded = false
                    readyToLogin.value = true
                }, currentUser, users)
            }
            if (readyToLogin.value) {
                authForm(currentUser.value, users)
            }
        }

    }

    @Composable
    fun userDropDownMenu(
        expanded: Boolean,
        onDismiss: () -> Unit,
        state: MutableState<String>,
        userList: List<UserInfoDto>
    ) = DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        for (user in userList) {
            DropdownMenuItem(onClick = { state.value = user.email },
                content = { Text(user.email) })
        }
    }

    @Composable
    fun authForm(email: String, users: List<UserInfoDto>) {
        var password by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        OutlinedTextField(
            password,
            onValueChange = { password = it },
            placeholder = { Text("Пароль") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation())
        Button({
            scope.launch {
                val isAuth = source.authUser(email, password)
                println(isAuth)
                println(email)
                println(password)
                if(isAuth){
                    navigateState.value = GlobalPages.Note
                    userState.value = users.first { it.email == email }
                    println(userState.value.toString())
                }
            }
        }) {
            robotoText("Войти", color = Color.White)
        }
    }
}