package barry.com.weatherapp.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.AbsListView
import android.widget.AdapterView
import barry.com.weatherapp.R
import barry.com.weatherapp.model.DailyWeather
import barry.com.weatherapp.model.FeaturedWeather
import barry.com.weatherapp.ui.BaseActivity
import barry.com.weatherapp.ui.CustomAdapter
import barry.com.weatherapp.ui.weatherdetails.WeatherDetailsActivity
import barry.com.weatherapp.utils.ForecastImage
import kotlinx.android.synthetic.main.activity_main.*


open class MainActivity : BaseActivity(), View {

    private lateinit var presenterImpl: MainPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        presenterImpl = MainPresenterImpl(this)
    }

    override fun setupViews() {
        if (isLocationPermissionGranted()) {
            Log.d(TAG, "3 permission granted")
            if (isGpsEnabled()) {
                requestLocation()
            } else {
                buildAlertMessageNoGps()
                hideProgress()
            }
        } else {
            shouldShowRequestPermissionRationale()
        }

        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
        refreshLayout.setOnRefreshListener {
            if (isLocationPermissionGranted()) {
                if (isGpsEnabled()) {
                    requestLocation()
                } else {
                    buildAlertMessageNoGps()
                    hideProgress()
                }
            } else {
                showError(getString(R.string.enable_location))
                hideProgress()
            }
        }

        listViewForecast.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {

            }

            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (!refreshLayout.isRefreshing) {
                    if (listViewForecast.getChildAt(0) != null) {
                        refreshLayout.isEnabled = listViewForecast.firstVisiblePosition ==
                                0 && listViewForecast.getChildAt(0).top == 0
                    }
                }
            }
        })
    }

    override fun showFeaturedWeather(featuredWeather: FeaturedWeather) {
        layoutFeatured.visibility = android.view.View.VISIBLE
        imageForecast.setImageResource(ForecastImage.getImage(featuredWeather.icon))
        textDate.text = featuredWeather.dateTime
        textCity.text = featuredWeather.timezone
        textTemperature.text = String.format(getString(R.string.temp_value), featuredWeather.temperature)
        textSummary.text = featuredWeather.summary
    }

    override fun showWeatherList(listItems: ArrayList<DailyWeather>) {
        listViewForecast.visibility = android.view.View.VISIBLE
        listViewForecast.adapter = CustomAdapter(this, listItems)
        listViewForecast.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (!refreshLayout.isRefreshing) {
                goToDetailsActivity(listItems[position])
            }
        }
    }

    override fun showProgress() {
        refreshLayout.isRefreshing = true
    }

    override fun hideProgress() {
        refreshLayout.isRefreshing = false
    }

    override fun showError(message: String) {
        Snackbar.make(refreshLayout, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ENABLE_LOCATION_REQUEST -> {
                if (isGpsEnabled()) {
                    requestLocation()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d(TAG, "4 permission granted")
                    if (isGpsEnabled()) {
                        requestLocation()
                    } else {
                        buildAlertMessageNoGps()
                        hideProgress()
                    }
                } else {
                    Log.d(TAG, "5 permission denied")
                    showError(getString(R.string.enable_location))
                }
                return
            }
        }
    }

    private fun goToDetailsActivity(dailyWeather: DailyWeather) {
        val intent = Intent(this, WeatherDetailsActivity::class.java)
        intent.putExtra(WeatherDetailsActivity.EXTRA_WEATHER_DATA, dailyWeather)
        startActivity(intent)
    }

    private fun requestLocation() {
        startLocationUpdates()
        showProgress()
    }

    override fun onLocationChanged(location: Location) {
        super.onLocationChanged(location)
        presenterImpl.callForecastApi(location)
    }

}
