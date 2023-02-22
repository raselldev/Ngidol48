package com.applandeo.materialcalendarview.utils

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.applandeo.materialcalendarview.R
import com.applandeo.materialcalendarview.databinding.CalendarViewBinding


/**
 * Created by Applandeo Team.
 */

internal fun View.setAbbreviationsLabels(
    color: Int,
    firstDayOfWeek: Int,
    binding: CalendarViewBinding
) {
    val binding: CalendarViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.calendar_view, null, false)
    val labels = getAbbreviationsTextViews(binding)

    val abbreviations = context.resources.getStringArray(R.array.material_calendar_day_abbreviations_array)

    labels.forEachIndexed { index, label ->
        label.text = abbreviations[(index + firstDayOfWeek - 1) % 7]

        if (color != 0) {
            label.setTextColor(color)
        }
    }
}

internal fun View.setAbbreviationsLabelsSize(size: Float, binding: CalendarViewBinding) {
    val binding: CalendarViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.calendar_view, null, false)
    val labels = getAbbreviationsTextViews(binding)
    val maxTextSize = resources.getDimensionPixelSize(R.dimen.text_size_max)
    labels.forEachIndexed { _, label ->
        if (size > 0.0 && size <= maxTextSize) {
            label.textSize = size
        }
    }
}

private fun View.getAbbreviationsTextViews(binding: CalendarViewBinding) = listOf(
    binding.mondayLabel,
    binding.tuesdayLabel,
    binding.wednesdayLabel,
    binding.thursdayLabel,
    binding.fridayLabel,
    binding.saturdayLabel,
    binding.sundayLabel)

internal fun View.setTypeface(typeface: Typeface?, binding: CalendarViewBinding) {
    if (typeface == null) return
    val binding: CalendarViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.calendar_view, null, false)
    getAbbreviationsTextViews(binding).forEach { label ->
        label.typeface = typeface
    }
}

internal fun View.setHeaderColor(color: Int, binding: CalendarViewBinding) {
    if (color == 0) return
    binding.calendarHeader.setBackgroundColor(color)
}

internal fun View.setHeaderLabelColor(color: Int, binding: CalendarViewBinding) {
    if (color == 0) return
    binding.currentDateLabel.setTextColor(color)
}

internal fun View.setHeaderTypeface(typeface: Typeface?, binding: CalendarViewBinding) {
    if (typeface == null) return
    binding.currentDateLabel.typeface = typeface
}

internal fun View.setAbbreviationsBarColor(color: Int, binding: CalendarViewBinding) {
    if (color == 0) return
    binding.abbreviationsBar.setBackgroundColor(color)
}

internal fun View.setPagesColor(color: Int, binding: CalendarViewBinding) {
    if (color == 0) return
    binding.calendarViewPager.setBackgroundColor(color)
}

internal fun View.setPreviousButtonImage(drawable: Drawable?, binding: CalendarViewBinding) {
    if (drawable == null) return
    binding.previousButton.setImageDrawable(drawable)
}

internal fun View.setForwardButtonImage(drawable: Drawable?, binding: CalendarViewBinding) {
    if (drawable == null) return
    binding.forwardButton.setImageDrawable(drawable)
}

internal fun View.setHeaderVisibility(visibility: Int, binding: CalendarViewBinding) {
    binding.calendarHeader.visibility = visibility
}

internal fun View.setNavigationVisibility(visibility: Int, binding: CalendarViewBinding) {
    binding.previousButton.visibility = visibility
    binding.forwardButton.visibility = visibility
}

internal fun View.setAbbreviationsBarVisibility(visibility: Int, binding: CalendarViewBinding) {
    binding.abbreviationsBar.visibility = visibility
}
