package com.example.schoolapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.schoolapp.activities.ContactUsActivity
import com.example.schoolapp.activities.LoginActivity
import com.example.schoolapp.activities.MyAccountActivity
import com.example.schoolapp.activities.NotificationsActivity
import com.example.schoolapp.classes.BannerPagerAdapter
import com.example.schoolapp.classes.DashboardAdapter
import com.example.schoolapp.classes.DashboardItem
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.classes.SmoothDrawerToggle
import com.example.schoolapp.databinding.ActivityMainBinding
import com.example.schoolapp.databinding.NavHeaderBinding
import com.example.schoolapp.fragments.TimeTableFragment
import com.example.schoolapp.networks.NetworkChangeReceiver
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationHeaderBinding: NavHeaderBinding
    private lateinit var bannerPagerAdapter: BannerPagerAdapter

    var networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
        override fun onNetworkConnected() {
            Log.d("networkInterceptorTAG", "inside onNetworkConnected()")

        }

        override fun onNetworkDisconnected() {
            Log.d("networkInterceptorTAG", "inside onNetworkDisconnected()")
            Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_LONG).show()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialise()
        listeners()
        setupNavigationDrawer()
        setupDashboardRecyclerView()
        showCurrentDayAndDate()
        applyAnimations()
        setUpTimeTableFragment()


    }

    private fun initialise() {

        val headerView = binding.navigationView.getHeaderView(0)
        navigationHeaderBinding = NavHeaderBinding.bind(headerView)

        if (PrefsManager.getUserInformation(this@MainActivity) != null) {
            navigationHeaderBinding.tvStudentEmail.text =
                PrefsManager.getUserInformation(this@MainActivity).data.email
            navigationHeaderBinding.tvStudentName.text =
                PrefsManager.getUserInformation(this@MainActivity).data.name
            binding.tvWelcomeHeader.text =
                "Welcome, ${PrefsManager.getUserInformation(this@MainActivity).data.name}"
        }


    }

    private fun listeners() {
        binding.ivNotifcation.setOnClickListener {
            startActivity(Intent(this@MainActivity, NotificationsActivity::class.java))
        }
    }

    private fun setupNavigationDrawer() {

        val smoothDrawerToggle = SmoothDrawerToggle(binding.drawerLayout)
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item clicks
            when (menuItem.itemId) {
                R.id.nav_home -> {

                }

                R.id.nav_account -> {
                    startActivity(Intent(this@MainActivity, MyAccountActivity::class.java))
                }
                // Handle other menu items
                R.id.nav_contact -> {
                    startActivity(Intent(this@MainActivity, ContactUsActivity::class.java))
                }
            }

            // Close drawer with custom animation
            smoothDrawerToggle.closeDrawer()
            true
        }

        // Set up drawer toggle with custom animation
        binding.ivHamburger.setOnClickListener {
            smoothDrawerToggle.openDrawer()
        }

        navigationHeaderBinding.btnCloseDrawer.setOnClickListener {
            smoothDrawerToggle.closeDrawer()
        }

        binding.btnLogout.setOnClickListener {
            // Handle logout
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finishAffinity()
            getSharedPreferences(PrefsManager.PREF_NAME, Context.MODE_PRIVATE).edit() { clear() }
        }

    }

    private fun showCurrentDayAndDate() {

        binding.tvDate.text = SimpleDateFormat(
            "EEEE, MMMM d, yyyy",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
//        binding.tvDay.text = SimpleDateFormat("EEEE", Locale.getDefault()).format(Calendar.getInstance().time)
    }


    private fun setupDashboardRecyclerView() {
        val layoutManager = GridLayoutManager(this, 3)
        binding.rvDashboard.layoutManager = layoutManager
        val adapter = DashboardAdapter(this, getDashboardItems())
        binding.rvDashboard.adapter = adapter

        // Apply layout animation
        val animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down)
        binding.rvDashboard.layoutAnimation = animation
    }

    private fun applyAnimations() {

        // Animate profile image with rotation
        navigationHeaderBinding.profileImage.alpha = 0f
        navigationHeaderBinding.profileImage.scaleX = 0.5f
        navigationHeaderBinding.profileImage.scaleY = 0.5f
        navigationHeaderBinding.profileImage.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setInterpolator(OvershootInterpolator())
            .setStartDelay(300)
            .start()

        // Animate dashboard grid items sequentially
        binding.rvDashboard.scheduleLayoutAnimation()
    }

    private fun getDashboardItems(): List<DashboardItem> {
        return listOf(
            DashboardItem(R.drawable.ic_notice_board, "Notice Board"),
            DashboardItem(R.drawable.ic_attendance, "Attendance Register"),
            DashboardItem(R.drawable.ic_timetable, "My Timetable"),
            DashboardItem(R.drawable.ic_report_card, "My Exam Marks"),
            DashboardItem(R.drawable.moneyrupee, "Fee Deposit"),
            DashboardItem(R.drawable.ic_subject_notes, "Subject Notes"),
            DashboardItem(R.drawable.ic_calendar, "Academic Calendar"),
            DashboardItem(R.drawable.ic_leave_request, "Leave Request"),
            DashboardItem(R.drawable.ic_pta, "PTA"),
            DashboardItem(R.drawable.ic_homework, "Home Work"),
            DashboardItem(R.drawable.ic_grievance, "Grievance"),
            DashboardItem(R.drawable.ic_activity, "Extra Activity"),
            DashboardItem(R.drawable.ic_comment, "Student Comment")
        )
    }

    private fun setUpTimeTableFragment() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.findFragmentById(R.id.fragment_time_table_container) == null) {
            val timeTableFragment = TimeTableFragment()
            val transaction = fragmentManager.beginTransaction()
            transaction.add(
                R.id.fragment_time_table_container,
                timeTableFragment,
                "default_fragment"
            )
            transaction.commit()
        }
    }

    override fun onResume() {
        super.onResume()
        NetworkChangeReceiver.registerReceiver(this, networkChangeReceiver)
    }

    override fun onPause() {
        super.onPause()
        NetworkChangeReceiver.unregisterReceiver(this, networkChangeReceiver)
    }


}