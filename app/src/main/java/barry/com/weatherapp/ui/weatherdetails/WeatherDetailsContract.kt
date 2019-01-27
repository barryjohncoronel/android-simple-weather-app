package barry.com.weatherapp.ui.weatherdetails

import barry.com.weatherapp.model.DailyWeather
import barry.com.weatherapp.ui.BaseView

interface View : BaseView {
    fun showDetails(weather: DailyWeather)
}

interface Presenter {
    fun validateDetails(weather: DailyWeather?)
}