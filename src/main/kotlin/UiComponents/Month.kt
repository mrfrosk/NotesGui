package UiComponents

import java.time.LocalDate

enum class Month(val monthName: String, val days: Int, val stringNumber: String) {
    January("Январь", 31, "01"),
    February("Февраль", if (LocalDate.now().year % 4 == 0) 28 else 29, "02"),
    March("Март", 31, "03"),
    April("Апрель", 30, "04"),
    May("Май", 31, "05"),
    June("Июнь", 30, "06"),
    July("Июль", 31, "07"),
    August("Август", 31, "08"),
    September("Сентябрь", 30, "09"),
    October("Октрябрь", 31, "10"),
    November("Ноябрь", 30, "11"),
    December("Декабрь", 31, "12");

    companion object{
        fun getByName(name: String): Month? {
            return Month.entries.find { it.monthName == name }
        }
    }

}