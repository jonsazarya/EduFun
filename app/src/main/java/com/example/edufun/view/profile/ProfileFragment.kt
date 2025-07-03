package com.example.edufun.view.profile

import android.app.DatePickerDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.edufun.R
import com.example.edufun.view.main.MainViewModel
import com.example.edufun.model.User
import com.example.edufun.notification.NotificationReceiver
import java.util.Calendar

class ProfileFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var profileTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileTextView = view.findViewById(R.id.text_profile)
        logoutButton = view.findViewById(R.id.btn_logout)

        mainViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            updateUI(user)
        })

        logoutButton.setOnClickListener {
            mainViewModel.logout()
            showNotification("Logout", "Kamu berhasil keluar dari EduFun.")
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        }

        val calendarItem = view.findViewById<ConstraintLayout>(R.id.calendar_item)
        calendarItem.setOnClickListener {
            showCalendar()
        }

        val shareItem = view.findViewById<ConstraintLayout>(R.id.share_item)
        shareItem.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Coba Aplikasi EduFun")
                putExtra(Intent.EXTRA_TEXT, "Yuk coba aplikasi belajar interaktif EduFun! 📚✨")
            }
            startActivity(Intent.createChooser(shareIntent, "Bagikan EduFun melalui"))
        }

        val settingItem = view.findViewById<ConstraintLayout>(R.id.setting_item)
        settingItem.setOnClickListener {
            val settingsIntent = Intent(Settings.ACTION_SETTINGS)
            startActivity(settingsIntent)
        }

        createNotificationChannel()
        scheduleDailyReminder()
        return view
    }

    private fun updateUI(user: User) {
        profileTextView.text = user.email.ifEmpty { "Profile Information" }
    }

    private fun showCalendar() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                Toast.makeText(
                    requireContext(),
                    "Tanggal dipilih: $selectedDay/${selectedMonth + 1}/$selectedYear",
                    Toast.LENGTH_SHORT
                ).show()
                showNotification("Pengingat", "Kamu memilih tanggal $selectedDay/${selectedMonth + 1}/$selectedYear")
            }, year, month, day)
        datePickerDialog.show()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(requireContext(), "edufun_channel")
            .setSmallIcon(R.drawable.baseline_notifications_24) // pastikan ikon ini ada di drawable
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "EduFun Channel"
            val descriptionText = "Channel untuk notifikasi EduFun"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = android.app.NotificationChannel("edufun_channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleDailyReminder() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = android.app.PendingIntent.getBroadcast(
            requireContext(), 0, intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(java.util.Calendar.HOUR_OF_DAY, 10)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
        }

        // Jika waktu sekarang sudah lewat 10 pagi, setel untuk besok
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setRepeating(
            android.app.AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            android.app.AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }


    companion object {
        private const val NOTIFICATION_ID = 1
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
