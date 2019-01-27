package barry.com.weatherapp.ui.main

import android.location.Location
import barry.com.weatherapp.model.DailyWeather
import barry.com.weatherapp.model.FeaturedWeather
import barry.com.weatherapp.ui.BaseView


interface View : BaseView {
    fun showFeaturedWeather(featuredWeather: FeaturedWeather)
    fun showWeatherList(listItems: ArrayList<DailyWeather>)
}

interface Presenter {
    fun callForecastApi(location: Location)
}