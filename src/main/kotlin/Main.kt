import Pages.MainPage
import Pages.enums.GlobalPages
import UiComponents.clickableText
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


@Composable
@Preview
fun App() {

    mainScreen()
}

@Composable
fun mainScreen() {
    var page by remember { mutableStateOf(GlobalPages.Note) }
    Row {
        Column {
            clickableText("Заметки", action = { page = GlobalPages.Note })
            clickableText("Тикеты", action = { page = GlobalPages.Ticket })
        }
        when (page) {
            GlobalPages.Note -> ticketPage()
            GlobalPages.Ticket -> ticketPage()
            GlobalPages.Auth -> TODO()
        }
    }
}


@Composable
fun ticketPage() {
    Column {
        Text("Тикеты")
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        val mainPage = MainPage()
        mainPage.draw()
    }
}

