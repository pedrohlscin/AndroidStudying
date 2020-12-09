package com.pedroso.beelog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedroso.beelog.database.data.Location

class LocationListAdapter : ListAdapter<Location, LocationListAdapter.LocationViewHolder>(LocationsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.longitude.toString(), current.latitude.toString())
    }

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val latitudeItemView: TextView = itemView.findViewById(R.id.textViewLatitude)
        private val longitudeItemView: TextView = itemView.findViewById(R.id.textViewLongitude)

        fun bind(longitude: String?, latitude: String? ) {
            latitudeItemView.text = latitude
            longitudeItemView.text = longitude
        }

        companion object {
            fun create(parent: ViewGroup): LocationViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return LocationViewHolder(view)
            }
        }
    }

    class LocationsComparator : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id && oldItem.longitude == newItem.longitude && oldItem.latitude == newItem.latitude
        }
    }
}