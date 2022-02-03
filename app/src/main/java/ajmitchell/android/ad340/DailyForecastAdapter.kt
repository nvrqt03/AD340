package ajmitchell.android.ad340

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DailyForecastAdapter(
    private val clickHandler: (DailyForecast) -> Unit // this is how you implement a click handler, for when an item is clicked
): ListAdapter<DailyForecast, DailyForecastViewHolder>(DIFF_CONFIG) {

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<DailyForecast>() {
            // ? WTF??
            // do object here is an object expression. We're essentialy creating an anonymous inner class.
            // we're creating a new instance of an anonymous inner class. the : means we are extending some other class,
            // in this case DiffUtil.ItemCallback. we are passing in DailyForecast as a templated type, saying that
            // thsi item callback will work on DailyForecast items. now lets implement methods

            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
                return oldItem === newItem // the === tells us if they are the same
            }

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                return oldItem == newItem // using == will tell us if they contents are the same, but not if they're the same object
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        // creates a new viewholder. we'll creat a new view that will be used to represent each item in our list
        // now lets inflate our layouts
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_forecast, parent, false)
        return DailyForecastViewHolder(itemView)
    }
// so now anytime the recyclerView needs to create a new viewHolder, its going to call onCreateViewHolder.
    // and anytime it needs to bind a viewholder so that it can put new info on the screen, it will call onBindViewHolder

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        // each individual element of forecast items and pass that data to the viewholder so the items can be update. but we need a
        // layout to represent our list items

        // in onBindViewHolder, we're getting passed back an instance of teh viewHolder we just created. so we can type
        holder.bind(getItem(position))
        // we need to get access to a dailyForecast item. how? this is where the listAdapter we extended comes in handy. it will store
        // a list of these dailyForecast items, that's why we define dailyForecast item as the type of items listAdapter would work with

        // now lets implement the click handler
        holder.itemView.setOnClickListener{
            clickHandler(getItem(position))
        }
    }
}

class DailyForecastViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    // our viewHolder takes one parameter, and passes that into our parent class

    private val tempText = view.findViewById<TextView>(R.id.tempText) // notice how we specified the type here
    private val descriptionText : TextView = view.findViewById(R.id.descriptionText)

    // now we create a bind that will bind data to the two view items we just referenced
    fun bind(dailyForecast: DailyForecast) {
        tempText.text = String.format("%.2f", dailyForecast.temp)
        descriptionText.text = dailyForecast.description.toString()
    }
// now that we've taken care of of everything, lets go mainActivity and attach the adapter to the recyclerView
}