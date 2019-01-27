package barry.com.weatherapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import barry.com.weatherapp.R
import barry.com.weatherapp.model.DailyWeather
import barry.com.weatherapp.utils.ForecastImage

class CustomAdapter(private val context: Context, private val forecastList: ArrayList<DailyWeather>) :
    BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ForecastViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_row_item_forecast, parent, false)
            vh = ForecastViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ForecastViewHolder
        }

        vh.apply {
            textDate.text = forecastList[position].dateTime
            imageForecast.setImageResource(ForecastImage.getImage(forecastList[position].icon))
            textDescription.text = forecastList[position].summary
        }

        return view
    }

    override fun getItem(position: Int) = forecastList[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = forecastList.size

    private class ForecastViewHolder(view: View) {
        val textDate = view.findViewById(R.id.textDate) as TextView
        val imageForecast = view.findViewById(R.id.imageForecast) as ImageView
        val textDescription = view.findViewById(R.id.textDescription) as TextView
    }

}