package com.arira.ngidol48.ui.activity.newKalender

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.utils.DayClick
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.EventKalenderAdapter
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.ActivityKalenderBinding
import com.arira.ngidol48.databinding.SheetEventKalenderBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.model.Event
import com.arira.ngidol48.ui.activity.event.EventViewModel
import com.arira.ngidol48.utilities.DrawableUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*


class KalenderActivity : BaseActivity(), DayClick{
    lateinit var binding: ActivityKalenderBinding
    private lateinit var viewModel: EventViewModel
    private var listEvent:ArrayList<Event> = ArrayList()
    private var newListEvent:ArrayList<Event> = ArrayList()

    override fun onDayClick(tanggal: String, bulan: String) {
        val jadwal = findSameDate(tanggal, bulan)

        if (jadwal.isEmpty()){
            toast.show(getString(R.string.teks_tidak_ada_jadwal_pada_tanggal_s, "${tanggal} ${bulan}"), this)
        }else{
            showSheetScedule(findSameDate(tanggal, bulan), tanggal, bulan)
        }


    }

    private fun showSheetScedule(list:List<Event>, tanggal:String, bulan:String){
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.dismissWithAnimation = true
        val sheetBinding: SheetEventKalenderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.sheet_event_kalender, null, false)
        dialog.setContentView(sheetBinding.root)
        sheetBinding.tvJudul.text = "Jadwal ${tanggal} ${bulan}"
        sheetBinding.rvData.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            adapter = EventKalenderAdapter(list)
        }
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalender)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_kalender)
        setToolbar( getString(R.string.teks_kalender_jadwal), binding.toolbar)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EventViewModel::class.java]
        viewModel.context = this

        action()
        observerData()
        viewModel.hitAll()

        if (pref.getNewCalender()){
            binding.linNewCalender.visibility = View.GONE
        }else{
            binding.linNewCalender.visibility = View.VISIBLE
            binding.switchChangeToMain.isChecked = pref.getNewCalender()
        }
    }

    private fun findSameDate(tanggal: String, bulan: String): List<Event> {
//        val sameDate:ArrayList<Event> = ArrayList()

        return listEvent.filter { model -> tanggal == model.tanggal && bulan == model.bulan_tahun }
    }

    private fun loadToKalender(){

        //filtering & grouping
        for (event in listEvent){
            Log.e("FILTERING", "tanggal: ${event.tanggal} || bulan_tahun : ${event.bulan_tahun}")

            if (newListEvent.isNotEmpty()){
                val lastEvent = newListEvent.last()


                if (lastEvent.tanggal == event.tanggal && lastEvent.bulan_tahun == event.bulan_tahun){
                    Log.e("FILTERING", "---a")
                    lastEvent.listEventName.add(event.event_name)
                    newListEvent[newListEvent.lastIndex] = lastEvent
                }else{
                    Log.e("FILTERING", "---b")
                    event.listEventName.add(event.event_name)
                    newListEvent.add(event)
                }
            }else{
                Log.e("FILTERING", "---c")
                event.listEventName.add(event.event_name)
                newListEvent.add(event)
            }

        }

        //create event list
        val events: ArrayList<EventDay> = ArrayList()
        for (event in newListEvent){
            val calendar1 = Calendar.getInstance()
            val newSdf = SimpleDateFormat("dd MMM yyyy", Locale("id"))

            calendar1.time = newSdf.parse("${event.tanggal} ${event.bulan_tahun}")

            if (event.listEventName.isNotEmpty()){
                events.add(
                    EventDay(
                        calendar1,
                        event.listEventName,
                        DrawableUtils.getCircleDrawableWithText(this, event.listEventName.size.toString()),
                        event.tanggal,
                        event.bulan_tahun,
                        this
                    )
                )
            }else{
                events.add(
                    EventDay(
                        calendar1,
                        event.listEventName,
                        event.tanggal,
                        event.bulan_tahun,
                        this
                    )
                )
            }

        }

        /*set events*/
        binding.kalender.setEvents(events)
    }


    fun action(){
        binding.switchChangeToMain.setOnClickListener {
            pref.setNewCalender(binding.switchChangeToMain.isChecked)
            if (binding.switchChangeToMain.isChecked){
                binding.linNewCalender.visibility = View.GONE
            }
        }

        binding.swipe.setOnRefreshListener {

            viewModel.hitAll()
        }

    }

    fun observerData(){
        viewModel.getLoading().observe(this) {
            it.let {
                binding.swipe.isRefreshing = it
            }
        }

        viewModel.getError().observe(this, Observer {
            it.let {
            }
        })

        viewModel.getResponse().observe(this, Observer {
            it.let {
                if (it != null) {
                    if (it.events.isNotEmpty()){
                        listEvent.clear()
                        listEvent.addAll(it.events)


                        loadToKalender()
                    }
                }
            }
        })
    }
}