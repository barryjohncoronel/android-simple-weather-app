package barry.com.weatherapp.model

data class FeaturedWeather(
    val timezone: String,
    val dateTime: String,
    val summary: String,
    val icon: String,
    val temperature: Double
)