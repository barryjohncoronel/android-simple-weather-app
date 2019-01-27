package barry.com.weatherapp.utils

import barry.com.weatherapp.model.DailyWeather
import barry.com.weatherapp.model.FeaturedWeather
import org.json.JSONObject
import java.util.*

object DataParser {
    private const val CURRENTLY = "currently"
    private const val DAILY = "daily"
    private const val DATA = "data"

    private const val TEMPERATURE = "temperature"
    private const val TIME = "time"
    private const val TIMEZONE = "timezone"
    private const val SUMMARY = "summary"
    private const val ICON = "icon"
    private const val SUNRISE_TIME = "sunriseTime"
    private const val SUNSET_TIME = "sunsetTime"
    private const val TEMP_HIGH = "temperatureHigh"
    private const val TEMP_LOW = "temperatureLow"
    private const val HUMIDITY = "humidity"
    private const val PRESSURE = "pressure"
    private const val WIND_SPEED = "windSpeed"

    fun getWeatherData(forecastJsonStr: String): ArrayList<DailyWeather> {

        val jsonObject = JSONObject(forecastJsonStr)

        val dailyObject = jsonObject.getJSONObject(DAILY)
        val dailyArray = dailyObject.getJSONArray(DATA)

        val dailyWeatherList = ArrayList<DailyWeather>()
        for (i in 0 until dailyArray.length()) {
            val dayForecast = dailyArray.getJSONObject(i)

            val gc = GregorianCalendar()
            gc.add(Calendar.DAY_OF_MONTH, i)

            var timezone = jsonObject.getString(TIMEZONE).replace("_", " ")
            val slashPos = timezone.indexOf("/")
            timezone = timezone.substring(slashPos + 1, timezone.length)

            val time = dayForecast.getInt(TIME)
            val summary = dayForecast.getString(SUMMARY)
            val icon = dayForecast.getString(ICON)
            val sunriseTime = dayForecast.getInt(SUNRISE_TIME)
            val sunsetTime = dayForecast.getInt(SUNSET_TIME)
            val temperatureHigh = dayForecast.getDouble(TEMP_HIGH)
            val temperatureLow = dayForecast.getDouble(TEMP_LOW)
            val humidity = dayForecast.getDouble(HUMIDITY)
            val pressure = dayForecast.getDouble(PRESSURE)
            val windSpeed = dayForecast.getDouble(WIND_SPEED)

            val day = DailyWeather(
                timezone,
                DateFormat.unixTimeStampToDateTime(
                    time,
                    DateFormat.MMM_DD_YYYY_EEE
                ),
                summary,
                icon,
                DateFormat.unixTimeStampToDateTime(
                    sunriseTime,
                    DateFormat.HH_MM
                ),
                DateFormat.unixTimeStampToDateTime(
                    sunsetTime,
                    DateFormat.HH_MM
                ),
                temperatureHigh,
                temperatureLow,
                humidity,
                pressure,
                windSpeed
            )

            dailyWeatherList.add(day)
        }

        return dailyWeatherList
    }

    fun getFeaturedWeather(forecastJsonStr: String): FeaturedWeather {
        val jsonObject = JSONObject(forecastJsonStr)
        val currentObject = jsonObject.getJSONObject(CURRENTLY)

        var timezone = jsonObject.getString(TIMEZONE).replace("_", " ")
        val slashPos = timezone.indexOf("/")
        timezone = timezone.substring(slashPos + 1, timezone.length)

        val time = currentObject.getInt(TIME)
        val summary = currentObject.getString(SUMMARY)
        val icon = currentObject.getString(ICON)
        val temperature = currentObject.getDouble(TEMPERATURE)

        return FeaturedWeather(
            timezone,
            DateFormat.unixTimeStampToDateTime(
                time,
                DateFormat.MMM_DD_YYYY_EEE
            ),
            summary,
            icon,
            temperature
        )
    }
}
