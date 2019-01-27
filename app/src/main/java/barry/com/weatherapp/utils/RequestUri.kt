package barry.com.weatherapp.utils

import android.location.Location
import android.net.Uri

object RequestUri {

    private const val API_KEY = "16c09ed289463a391691a14f6dbbe4ac"
    private const val BASE_URL = "api.darksky.net"
    private const val HTTPS = "https"
    private const val PATH_FORECAST = "forecast"
    private const val UNITS = "units"
    private const val UNIT_US = "si"
    private const val EXCLUDE = "exclude"
    private const val EXCLUDE_VALUE = "minutely,hourly,flags"

    fun getRequest(location: Location): String {
        val builder = Uri.Builder()
        builder.scheme(HTTPS)
            .authority(BASE_URL)
            .appendPath(PATH_FORECAST)
            .appendPath(API_KEY)
            .appendPath("${location.latitude},${location.longitude}")
            .appendQueryParameter(
                UNITS,
                UNIT_US
            )
            .appendQueryParameter(
                EXCLUDE,
                EXCLUDE_VALUE
            )
        return builder.build().toString()
    }

}