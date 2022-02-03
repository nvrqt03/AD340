package ajmitchell.android.ad340

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class ForecastRepository {
    // how will activity get data from our repo?

    // this is what we'll use to update data

    private val _weeklyForecast = MutableLiveData<List<DailyForecast>>()
    // what we have done is defined a private read only property and assigned it the value of a mutable live data
    // that holds a list of daily forecast items

    // we still need to make a way for the activity to listen for the update
    val weeklyForecast: LiveData<List<DailyForecast>> = _weeklyForecast
    // they look the same, but weeklyForecast is public, so our activity can get access to it.
    // also it's LiveData, instead of MutableLiveData. What this means is anything that references it (like
    // our MainActivityj) will get updates, but not publish it's own changes. we want the repository to be the
    // only place we can modify this data.

    // now we need to load the data
    fun loadForecast(zipcode: String) {
        // define a list of 7 random values representing our temp values
        val randomValues = List(10) { Random.nextFloat().rem(100) * 100}
        // Random will generate random numbers for us, but lets limit to 100

        // use this to create items to send to our liveData. Map allows us to take in a value and convert it to a different
        // output value
        val forecastItems = randomValues.map{ temp ->
            DailyForecast(temp, getTempDescription(temp))
        }
        // so what this has done is used each one of the random values (7) creates a list of forecast items with 7 items in it.
        // it - stands for each individual item. we'll change this to "temp". so now we have items that generate random temps, with
        // the same description. Now we send the list to our liveData
        _weeklyForecast.setValue(forecastItems) // now lets go to main activity and actually observe or listen for these
    }

    private fun getTempDescription(temp: Float) : String {
        return when (temp) {
            in Float.MIN_VALUE.rangeTo(0f) -> "Anything below 0 doesn't make sense"
            in 0f.rangeTo(32f) -> "Way to cold for your boy"
            in 33f.rangeTo(65f) -> "Kinda chilly"
            in 66f.rangeTo(80f) -> "Yaaaassss!!!"
            in 81f.rangeTo(90f) -> "Whew, it's kinda warm!"
            in 91f.rangeTo(100f) -> "Crank that AC fam!"
            in 100f.rangeTo(Float.MAX_VALUE) -> "Bro, what is HAPPENING"
            else -> "Does not compute"
        }
    }
}