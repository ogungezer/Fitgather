package com.example.fitgether.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitgether.R
import com.example.fitgether.data.model.Event
import com.example.fitgether.databinding.EventCardItemBinding

class HomepageEventsAdapter(private val eventList : List<Event>) : RecyclerView.Adapter<HomepageEventsAdapter.EventHolder>() {

    class EventHolder(val binding : EventCardItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val cardItemBinding = EventCardItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventHolder(cardItemBinding)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {

        holder.binding.eventItemImage.setImageResource(R.drawable.skating)
        holder.binding.eventItemTitle.text = eventList[position].eventTitle
        holder.binding.eventItemDescription.text = eventList[position].eventDescription
        holder.binding.eventItemPersonCount.text = eventList[position].joinedUserIds.size.toString()
        holder.binding.eventItemMaxPersonCount.text = eventList[position].maxPerson.toString()
        holder.binding.eventItemDate.text = eventList[position].eventDate
        holder.binding.eventItemTime.text = eventList[position].eventTime
        holder.binding.eventItemLocation.text = eventList[position].eventAddress
        //username, image, sonradan yapılacak
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

}