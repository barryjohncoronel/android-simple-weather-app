package barry.com.weatherapp.utils

import barry.com.weatherapp.R

enum class ForecastImage(val image: String) {
    CLEAR_DAY("clear-day"),
    CLEAR_NIGHT("clear-night"),
    RAIN("rain"),
    SNOW("snow"),
    SLEET("sleet"),
    WIND("wind"),
    FOG("fog"),
    CLOUDY("cloudy"),
    PARTLY_CLOUDY_DAY("partly-cloudy-day"),
    PARTLY_CLOUDY_NIGHT("partly-cloudy-night"),
    HAIL("hail"),
    THUNDERSTORM("thunderstorm"),
    TORNADO("tornado");

    companion object {
        fun getImage(icon: String): Int {
            return when (icon) {
                CLEAR_DAY.image -> R.drawable.ic_clear_day
                CLEAR_NIGHT.image -> R.drawable.ic_clear_night
                RAIN.image -> R.drawable.ic_rain
                SNOW.image,
                SLEET.image -> R.drawable.ic_snow
                WIND.image -> R.drawable.ic_windy
                FOG.image -> R.drawable.ic_fog
                CLOUDY.image -> R.drawable.ic_cloudy
                PARTLY_CLOUDY_DAY.image,
                PARTLY_CLOUDY_NIGHT.image -> R.drawable.ic_partly_cloudy
                HAIL.image -> R.drawable.ic_hail
                THUNDERSTORM.image -> R.drawable.ic_thunderstorm
                TORNADO.image -> R.drawable.ic_tornado
                else -> R.drawable.ic_clear_day
            }
        }
    }
}