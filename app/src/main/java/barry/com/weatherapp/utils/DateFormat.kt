package barry.com.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*

enum class DateFormat(val value: String) {
    MMM_DD_YYYY_EEE("MMM dd, yyyy EEE"),
    HH_MM("hh:mm");

    companion object {
         fun unixTimeStampToDateTime(unixTimeStamp: Int, format: DateFormat): String {
            val dateFormat = SimpleDateFormat(format.value, Locale.getDefault())
            val date = Date(unixTimeStamp * 1000.toLong())

            return dateFormat.format(date)
        }
    }
}