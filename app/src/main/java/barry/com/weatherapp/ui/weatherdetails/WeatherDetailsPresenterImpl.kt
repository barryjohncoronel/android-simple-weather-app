package barry.com.weatherapp.ui.weatherdetails

import barry.com.weatherapp.model.DailyWeather

class WeatherDetailsPresenterImpl(private val weatherDetailsView: View) : Presenter {

    override fun validateDetails(weather: DailyWeather?) {
        if (weather != null) {
            weatherDetailsView.showDetails(weather)
        } else {
            weatherDetailsView.showError("Something went wrong. Try to refresh data in Dashboard.")
        }
    }
}