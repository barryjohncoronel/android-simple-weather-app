package barry.com.weatherapp.ui.main

import android.annotation.SuppressLint
import android.location.Location
import android.os.AsyncTask
import android.util.Log
import barry.com.weatherapp.utils.RequestUri
import barry.com.weatherapp.model.DailyWeather
import barry.com.weatherapp.model.FeaturedWeather
import barry.com.weatherapp.utils.DataParser
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainPresenterImpl(private val mainView: View) : Presenter {

    private val TAG = javaClass.simpleName
    private lateinit var featuredWeather: FeaturedWeather

    override fun callForecastApi(location: Location) {
        WeatherTask().execute(location)
    }

    @SuppressLint("StaticFieldLeak")
    inner class WeatherTask : AsyncTask<Location, Void, ArrayList<DailyWeather>>() {

        override fun doInBackground(vararg params: Location): ArrayList<DailyWeather>? {
            return getTenDayForecast(params[0])
        }

        private fun getTenDayForecast(location: Location): ArrayList<DailyWeather>? {
            var forecastDays: ArrayList<DailyWeather>? = null
            val forecastResponse: String

            try {
                val url = URL(RequestUri.getRequest(location))

                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                val inputStream = urlConnection.inputStream
                val buffer = StringBuilder()
                if (inputStream == null) {
                    mainView.showError("Something went wrong")
                    return null
                }
                val reader = BufferedReader(InputStreamReader(inputStream))
                val line = reader.readLine()
                buffer.append(line).append("\n")

                if (buffer.isEmpty()) {
                    mainView.showError("Something went wrong")
                    return null
                }
                forecastResponse = buffer.toString()

                try {
                    Log.d(TAG, "actual response: $forecastResponse")
                    forecastDays = DataParser.getWeatherData(forecastResponse)
                    featuredWeather = DataParser.getFeaturedWeather(forecastResponse)
                } catch (je: JSONException) {
                    Log.e(TAG, "getTenDayForecast()_JSONException: ${je.localizedMessage}")
                    mainView.showError("Something went wrong - ${je.localizedMessage}")
                }

                try {
                    reader.close()
                } catch (ioe: IOException) {
                    Log.e(TAG, "getTenDayForecast()IOException: ${ioe.localizedMessage}")
                    mainView.showError("Something went wrong - ${ioe.localizedMessage}")
                    return null
                }
                urlConnection.disconnect()

            } catch (ioe: IOException) {
                Log.e(TAG, "getTenDayForecast()IOException: ${ioe.localizedMessage}")
                mainView.showError("Something went wrong - ${ioe.localizedMessage}")
                return null
            }

            return forecastDays
        }

        override fun onPostExecute(result: ArrayList<DailyWeather>?) {
            Log.d(TAG, "onPostExecute()_result: $result")

            if (result != null) {
                mainView.showFeaturedWeather(featuredWeather)

                result.forEach {
                    it.windSpeed *= 3.6 //converts to kph
                }

                mainView.showWeatherList(result) //TODO bars
            }

            mainView.hideProgress()

            super.onPostExecute(result)
        }
    }

}