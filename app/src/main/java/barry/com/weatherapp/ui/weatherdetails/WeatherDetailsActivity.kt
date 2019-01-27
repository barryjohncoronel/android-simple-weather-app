package barry.com.weatherapp.ui.weatherdetails

import android.os.Bundle
import android.support.design.widget.Snackbar
import barry.com.weatherapp.R
import barry.com.weatherapp.model.DailyWeather
import barry.com.weatherapp.ui.BaseActivity
import barry.com.weatherapp.utils.ForecastImage
import kotlinx.android.synthetic.main.activity_forecast_detail.*

class WeatherDetailsActivity : BaseActivity(), View {

    companion object {
        const val EXTRA_WEATHER_DATA = "extra_weather_data"
    }

    private lateinit var presenterImpl: WeatherDetailsPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_forecast_detail)
        super.onCreate(savedInstanceState)

        presenterImpl = WeatherDetailsPresenterImpl(this)
        presenterImpl.validateDetails(intent.getParcelableExtra(EXTRA_WEATHER_DATA))
    }

    override fun setupViews() {
        // do nothing
    }

    override fun showDetails(weather: DailyWeather) {
        weather.apply {
            setupToolbarTextTitleWithBack(dateTime)

            textCity.text = timezone
            imageForecast.setImageResource(ForecastImage.getImage(icon))
            textSummary.text = summary
            textSunriseValue.text = String.format(getString(R.string.sunrise_value), sunriseTime)
            textSunsetValue.text = String.format(getString(R.string.sunset_value), sunsetTime)
            textTempHighValue.text = String.format(getString(R.string.temp_value), temperatureHigh)
            textTempLowValue.text = String.format(getString(R.string.temp_value), temperatureLow)
            textHumidityValue.text = humidity.toString()
            textPressureValue.text = String.format(getString(R.string.pressure_value), pressure)
            textWindSpeedValue.text = String.format(getString(R.string.wind_speed_value), windSpeed)
        }

    }

    override fun showError(message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT).show()
    }
}
