package com.applandeo.materialcalendarview.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.R
import com.applandeo.materialcalendarview.databinding.CalendarViewDayBinding
import com.applandeo.materialcalendarview.exceptions.InvalidCustomLayoutException
import com.applandeo.materialcalendarview.utils.*
import java.util.*

private const val INVISIBLE_IMAGE_ALPHA = 0.12f

/**
 * This class is responsible for loading a one day cell.
 *
 *
 * Created by Applandeo team
 */
class CalendarDayAdapter(
        context: Context,
        private val calendarPageAdapter: CalendarPageAdapter,
        private val calendarProperties: CalendarProperties,
        dates: MutableList<Date>,
        pageMonth: Int,
        private val isDark:Boolean
) : ArrayAdapter<Date>(context, calendarProperties.itemLayoutResource, dates) {

    private val pageMonth = if (pageMonth < 0) 11 else pageMonth

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val dayView:CalendarViewDayBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.calendar_view_day, parent, false)

        val day = GregorianCalendar().apply { time = getItem(position) }

        dayView.dayIcon.visibility = View.GONE
        dayView.imgSize.loadIcon(day)
        dayView.rvEvent.loadEventName(day)

        val dayLabel = dayView.dayLabel ?: throw InvalidCustomLayoutException

        setLabelColors(dayLabel, day)
        dayLabel.typeface = calendarProperties.typeface
        dayLabel.text = day[Calendar.DAY_OF_MONTH].toString()

        dayLabel.rootView.setOnClickListener {
            calendarProperties.eventDays.firstOrNull { it.calendar == day }?.let { eventDay ->
                if(eventDay.callbackDayClick != null){
                    eventDay.callbackDayClick?.onDayClick(eventDay.tanggal, eventDay.bulan)
                }
            }
        }
        return dayView.root
    }

    private fun setLabelColors(dayLabel: TextView, day: Calendar) {
        when {
            // Setting not current month day color
            !day.isCurrentMonthDay() && !calendarProperties.selectionBetweenMonthsEnabled ->
                if (isDark){
                    dayLabel.setDayColors(calendarProperties.anotherMonthsDaysLabelsColorDark)
                }else{
                    dayLabel.setDayColors(calendarProperties.anotherMonthsDaysLabelsColor)
                }


            // Setting view for all SelectedDays
            day.isSelectedDay() -> {
                calendarPageAdapter.selectedDays
                        .firstOrNull { selectedDay -> selectedDay.calendar == day }
                        ?.let { selectedDay -> selectedDay.view = dayLabel }
                setSelectedDayColors(dayLabel, day, calendarProperties)
            }

            // Setting not current month day color only if selection between months is enabled for range picker
            !day.isCurrentMonthDay() && calendarProperties.selectionBetweenMonthsEnabled -> {
                if (SelectedDay(day) !in calendarPageAdapter.selectedDays) {
                    dayLabel.setDayColors(calendarProperties.anotherMonthsDaysLabelsColor)
                }
            }

            // Setting disabled days color
            !day.isActiveDay() -> dayLabel.setDayColors(calendarProperties.disabledDaysLabelsColor)

            // Setting custom label color for event day
            day.isEventDayWithLabelColor() -> setCurrentMonthDayColors(day, dayLabel, calendarProperties, isDark)

            // Setting current month day color
            else -> setCurrentMonthDayColors(day, dayLabel, calendarProperties, isDark)
        }
    }

    private fun Calendar.isSelectedDay() = calendarProperties.calendarType != CalendarView.CLASSIC
            && SelectedDay(this) in calendarPageAdapter.selectedDays
            && if (!calendarProperties.selectionBetweenMonthsEnabled) this[Calendar.MONTH] == pageMonth else true

    private fun Calendar.isEventDayWithLabelColor() = this.isEventDayWithLabelColor(calendarProperties)

    private fun Calendar.isCurrentMonthDay() = this[Calendar.MONTH] == pageMonth
            && !(calendarProperties.minimumDate != null
            && this.before(calendarProperties.minimumDate)
            || calendarProperties.maximumDate != null
            && this.after(calendarProperties.maximumDate))

    private fun Calendar.isActiveDay() = this !in calendarProperties.disabledDays

    private fun ImageView.loadIcon(day: Calendar) {
        if (!calendarProperties.eventsEnabled) {
            visibility = View.GONE
            return
        }

        calendarProperties.eventDays.firstOrNull { it.calendar == day }?.let { eventDay ->
            if(eventDay.imageDrawable != null){
                visibility = View.VISIBLE
                loadImage(eventDay.imageDrawable!!)
            }else{
                visibility = View.GONE
            }

            // If a day doesn't belong to current month then image is transparent
            if (!day.isCurrentMonthDay() || !day.isActiveDay()) {
                alpha = INVISIBLE_IMAGE_ALPHA
            }
        }
    }

    private fun RecyclerView.loadEventName(day: Calendar) {
        if (!calendarProperties.eventsEnabled) {
            visibility = View.GONE
            return
        }

        calendarProperties.eventDays.firstOrNull { it.calendar == day }?.let { eventDay ->
            layoutManager = LinearLayoutManager(context)
            adapter = AdapterEvent(eventDay.liveEventName)
            // If a day doesn't belong to current month then image is transparent
            if (!day.isCurrentMonthDay() || !day.isActiveDay()) {
                alpha = INVISIBLE_IMAGE_ALPHA
            }
        }
    }
}