package Pages

import Pages.enums.GlobalPages
import UiComponents.robotoText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import data.source.SpringDataSource
import data.source.ISource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfilePage(private val navigateState: MutableState<GlobalPages>, val userState: MutableState<String?>) {
    private val source: ISource = SpringDataSource()
    private val padding = 5.dp

    @Composable
    fun draw() {
        val scope = rememberCoroutineScope()
        val source = SpringDataSource()
        Column {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            OutlinedTextField(
                email,
                onValueChange = { email = it },
                placeholder = { robotoText("email") }
            )
            OutlinedTextField(
                password,
                onValueChange = { password = it },
                placeholder = { robotoText("Пароль") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(top = padding)
            )

            Row(Modifier.padding(top = padding)) {
                Button(
                    {
                        scope.launch {
                            val authStatus = source.authUser(email, password)
                            if (authStatus){
                                userState.value = email
                                navigateState.value = GlobalPages.Note
                            }
                        }
                    }
                ) {
                    robotoText("Войти", color = Color.White)
                }
                Button(
                    {},
                    modifier = Modifier.absolutePadding(left = padding)
                ) {
                    robotoText("зарегистрироваться", color = Color.White)
                }
            }

        }
    }

//    @Composable
//    fun draw() {
//        val scope = rememberCoroutineScope()
//        var expanded by remember { mutableStateOf(false) }
//        val users by mutableStateOf(mutableListOf<String>())
//        val currentUser = remember { mutableStateOf<String>("") }
//        val readyToLogin = remember { mutableStateOf(false) }
//        scope.launch {
//            users.addAll(async { source.getUsers() }.await())
//            if (currentUser.value == "") {
//                currentUser.value = users.first()
//            }
//        }
//
//        Column {
//            Column {
//                TextButton(onClick = { expanded = !expanded }) {
//                    robotoText("Текущий пользователь: ${currentUser.value}")
//                }
//                userDropDownMenu(expanded, {
//                    expanded = false
//                    readyToLogin.value = true
//                }, currentUser, users)
//            }
//            if (readyToLogin.value) {
//                authForm(currentUser.value, users)
//            }
//        }
//
//    }
//
//    @Composable
//    fun userDropDownMenu(
//        expanded: Boolean,
//        onDismiss: () -> Unit,
//        state: MutableState<String>,
//        userList: List<String>
//    ) = DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
//        for (user in userList) {
//            DropdownMenuItem(onClick = { state.value = user },
//                content = { Text(user) })
//        }
//    }
//
//    @Composable
//    fun authForm(email: String, users: List<String>) {
//        var newEmail by remember { mutableStateOf("") }
//        var password by remember { mutableStateOf("") }
//        val scope = rememberCoroutineScope()
//        var isCreate by remember { mutableStateOf(false) }
//        if (isCreate) {
//            OutlinedTextField(
//                newEmail,
//                onValueChange = { newEmail = it },
//                placeholder = { Text("Email") }
//            )
//        }
//        OutlinedTextField(
//            password,
//            onValueChange = { password = it },
//            placeholder = { Text("Пароль") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Row {
//            Button({
//                scope.launch {
//                    val isAuth = source.authUser(email, password)
//                    isCreate = false
//                    if (isAuth) {
//                        navigateState.value = GlobalPages.Note
//                        userState.value = users.first { it == email }
//                        println(userState.value.toString())
//                    }
//                }
//            }) {
//                robotoText("Войти", color = Color.White)
//            }
//            Spacer(Modifier.width(10.dp))
//            Button({
//                isCreate  = true
//                scope.launch {
//                }
//            }) {
//                robotoText("Зарегестрироваться", color = Color.White)
//            }
//        }
//    }

}