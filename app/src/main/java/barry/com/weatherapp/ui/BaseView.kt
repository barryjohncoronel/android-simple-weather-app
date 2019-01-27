package barry.com.weatherapp.ui

interface BaseView {
    fun showProgress() {

    }

    fun hideProgress() {

    }

    fun showError(message: String)
}