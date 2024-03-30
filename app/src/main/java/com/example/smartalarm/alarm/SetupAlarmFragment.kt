package com.example.smartalarm.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.smartalarm.AlarmReceiver
import com.example.smartalarm.databinding.FragmentSetupAlarmBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class SetupAlarmFragment : Fragment() {
    private var _binding: FragmentSetupAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var picker: MaterialTimePicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetupAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTimePicker()
        binding.addAlarmButton.setOnClickListener {
            showTimePicker()
        }

    }
private fun setupTimePicker(){
    picker =
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(10)
            .setInputMode(INPUT_MODE_KEYBOARD)
            .build()

    picker.addOnPositiveButtonClickListener {
        binding.alarmTimeText.text = "${picker.hour}:${picker.minute}"
        binding.alarmLinear.visibility = View.VISIBLE
        setAlarm(picker.hour,picker.minute)
    }

}
    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(hour: Int, minute: Int) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun showTimePicker() {
        picker.show(parentFragmentManager, "clock_picker")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}