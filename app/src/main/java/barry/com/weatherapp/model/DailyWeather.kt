package barry.com.weatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DailyWeather(
    val timezone: String,
    val dateTime: String,
    val summary: String,
    val icon: String,
    val sunriseTime: String,
    val sunsetTime: String,
    val temperatureHigh: Double,
    val temperatureLow: Double,
    val humidity: Double,
    val pressure: Double,
    var windSpeed: Double
) : Parcelable