package Pages

import Pages.enums.GlobalPages
import UiComponents.clickableText
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import data.dto.Session
import data.dto.UserInfoDto

class MainPage {
    private val currentPage = mutableStateOf(GlobalPages.Auth)
    private val profilePage = ProfilePage(currentPage)
    private val selectPage = NotePage()

    @Composable
    fun draw() {

        var currentPage by remember { currentPage }
        Row(Modifier.fillMaxSize()) {
            if (Session.email != null) {
                Column {
                    clickableText("Заметки", action = {currentPage = GlobalPages.Note})
                    clickableText("Тикеты", action = {currentPage = GlobalPages.Ticket})
                }
            }
            Box(Modifier.fillMaxSize()) {
                navigate(currentPage)
            }
        }

    }

    @Composable
    fun navigate(currentSection: GlobalPages) = when (currentSection) {
        GlobalPages.Auth -> profilePage.draw()
        GlobalPages.Note -> selectPage.draw()
        GlobalPages.Ticket -> null
    }
}