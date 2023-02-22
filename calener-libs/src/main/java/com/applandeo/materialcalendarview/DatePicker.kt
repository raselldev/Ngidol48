package com.applandeo.materialcalendarview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.applandeo.materialcalendarview.databinding.DatePickerDialogBinding
import com.applandeo.materialcalendarview.utils.CalendarProperties
import com.applandeo.materialcalendarview.utils.isMonthAfter
import com.applandeo.materialcalendarview.utils.isMonthBefore
import com.applandeo.materialcalendarview.utils.midnightCalendar

/**
 * This class is responsible for creating DatePicker dialog.
 *
 * Created by Applandeo Team.
 */

class DatePicker(
        private val context: Context,
        private val calendarProperties: CalendarProperties
) {
    private lateinit var binding:DatePickerDialogBinding

    fun show(): DatePicker {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.date_picker_dialog, null, false)
        val view = binding.root

        if (calendarProperties.pagesColor != 0) {
            view.setBackgroundColor(calendarProperties.pagesColor)
        }

        setTodayButtonVisibility(binding.todayButton)
        setDialogButtonsColors(binding.negativeButton, binding.todayButton)
        setOkButtonState(calendarProperties.calendarType == CalendarView.ONE_DAY_PICKER, binding.positiveButton)

        setDialogButtonsTypeface(view)

        calendarProperties.onSelectionAbilityListener = { enabled ->
            setOkButtonState(enabled, binding.positiveButton)
        }

        val calendarView = CalendarView(context = context, properties = calendarProperties)

        binding.calendarContainer.addView(calendarView)

        calendarProperties.calendar?.let {
            runCatching { calendarView.setDate(it) }
        }

        val alertDialog = AlertDialog.Builder(context).create().apply {
            setView(view)
        }

        binding.negativeButton.setOnClickListener { alertDialog.cancel() }

        binding.positiveButton.setOnClickListener {
            alertDialog.cancel()
            calendarProperties.onSelectDateListener?.onSelect(calendarView.selectedDates)
        }

        binding.todayButton.setOnClickListener { calendarView.showCurrentMonthPage() }

        alertDialog.show()

        return this
    }

    private fun setDialogButtonsTypeface(view: View) {
        calendarProperties.typeface?.let { typeface ->
            binding.todayButton.typeface = typeface
            binding.negativeButton.typeface = typeface
            binding.positiveButton.typeface = typeface
        }
    }

    private fun setDialogButtonsColors(negativeButton: AppCompatButton, todayButton: AppCompatButton) {
        if (calendarProperties.dialogButtonsColor != 0) {
            negativeButton.setTextColor(ContextCompat.getColor(context, calendarProperties.dialogButtonsColor))
            todayButton.setTextColor(ContextCompat.getColor(context, calendarProperties.dialogButtonsColor))
        }
    }

    private fun setOkButtonState(enabled: Boolean, okButton: AppCompatButton) {
        okButton.isEnabled = enabled

        if (calendarProperties.dialogButtonsColor == 0) return

        val stateResource = if (enabled) {
            calendarProperties.dialogButtonsColor
        } else {
            R.color.disabledDialogButtonColor
        }

        okButton.setTextColor(ContextCompat.getColor(context, stateResource))
    }

    private fun setTodayButtonVisibility(todayButton: AppCompatButton) {
        calendarProperties.maximumDate?.let {
            if (it.isMonthBefore(midnightCalendar) || it.isMonthAfter(midnightCalendar)) {
                todayButton.visibility = View.GONE
            }
        }
    }
}
