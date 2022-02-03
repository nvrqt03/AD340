package ajmitchell.android.ad340

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val forecastRepository = ForecastRepository()
    // region setup methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val zipcodeEditText: EditText = findViewById(R.id.zipcode_edit_text)
        val enterButton: Button = findViewById(R.id.button1)
        val imageView: ImageView = findViewById(R.id.image)
        val forecastList: RecyclerView = findViewById(R.id.forecastList)

        forecastList.layoutManager = LinearLayoutManager(this)

        // we could pass the function into DailyForecastAdapter parenthesis by adding the lambda, but here we're going to
        // take advantage of a kotlin feature that says if you pass in a function to another function (the clickhandler
        // function passed into the adapter) if the function you're passing is the last argument, it can go outside the ()
        // ex our DailyForecastAdapter only takes in a function parameter, so when you pass that function in you can do it
        // outside the parenthesis. its called trailing lambda syntax
        val dailyForecastAdapter = DailyForecastAdapter() { forecastItem ->
            // this is where we'll handle click feedback
            val msg = getString(R.string.forecast_clicked_format, forecastItem.temp, forecastItem.description)
            // it is an implicit reciever type, referring to whatever value is passed into the lambda
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        forecastList.adapter = dailyForecastAdapter // now go update the observer to update the recyclerView

        enterButton.setOnClickListener {
            val zipcode: String = zipcodeEditText.text.toString()

            if (zipcode.length != 5) {
                Toast.makeText(this, getString(R.string.zipcode_entry_error) , Toast.LENGTH_SHORT).show()
            } else {
                forecastRepository.loadForecast(zipcode)
            }
        }
        // add observer to repository

        val weeklyForecastObserver = Observer<List<DailyForecast>> { forecastItems ->
            // update our list adapter
            Toast.makeText(this, "Loaded Items", Toast.LENGTH_SHORT).show()
            dailyForecastAdapter.submitList(forecastItems) // submitList will let us send a new list of items that will update
            // what's on the screen
        }
        forecastRepository.weeklyForecast.observe(this,weeklyForecastObserver)
        // we've pased in a lifecycle owner which is mainActivity,and we've passed in weeklyForecastObserver. So
        // that observer will be updated anytime liveData changes in our repository. and since we've passed in that lifecycle
        // observer, any changes will be bound to the lifecycle of the activiy, so any loading is taking too long, it won't
        // return once the activity is destroyed, which is helpful for us and prevents many diff types of issues
    }



    // endregion teardown methods
}