package com.example.schoolapp.activities

import android.animation.ValueAnimator
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolapp.R
import com.example.schoolapp.adapters.SubjectAdapter
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.ActivityReportCardBinding
import com.example.schoolapp.networks.NetworkChangeReceiver
import com.example.schoolapp.requests.GetMarksRequest
import com.example.schoolapp.responses.GetExamDetailResponse
import com.example.schoolapp.responses.GetMarksResponse
import com.example.schoolapp.responses.MarksData
import com.example.schoolapp.responses.SubjectMark
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class ReportCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportCardBinding
    private lateinit var subjectAdapter: SubjectAdapter
    private var maxMarksToSent: Int = 100

    private var exams: List<Exam> = mutableListOf<Exam>()


    var networkChangeReceiver: NetworkChangeReceiver =
        NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
            override fun onNetworkConnected() {
                binding.llNoInternetFound.visibility = View.GONE
                binding.nestedScrollView.visibility = View.VISIBLE
                Log.d("networkInterceptorTAG", "inside onNetworkConnected()")
            }

            override fun onNetworkDisconnected() {
                binding.nestedScrollView.visibility = View.GONE
                binding.llNoInternetFound.visibility = View.VISIBLE
                Log.d("networkInterceptorTAG", "inside onNetworkDisconnected()")
                Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_LONG).show()
            }
        })


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReportCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()
    }

    private fun initialisers() {
        setupExamSpinner()
        setupClickListeners()
        applyAnimations()
    }


    private fun setupExamSpinner() {

        ApiClient.getExamDetailInstance.getExamDetail(
            "Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
            "ci_session=5b6qicv26lfo48kmvgnfpmqgllo5c579"
        ).enqueue(object : retrofit2.Callback<GetExamDetailResponse> {

            override fun onResponse(
                call: Call<GetExamDetailResponse>,
                response: Response<GetExamDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val s: GetExamDetailResponse? = response.body()
                    exams = mutableListOf<Exam>()
                    s?.data?.forEach { examData ->
                        (exams as MutableList<Exam>).add(
                            Exam(
                                examData.id,
                                examData.name,
                                examData.max_mark
                            )
                        )
                    }

                    val examNames = exams.map { it.name }

                    val adapter = ArrayAdapter(
                        this@ReportCardActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        examNames
                    )
                    (binding.spinnerExam as? AutoCompleteTextView)?.setAdapter(adapter)

                    Log.d(
                        "examdetailresponseTAG",
                        "Status: ${s?.status} + dataSize: ${s?.data?.size}"
                    )
                } else {
                    val examDetail: GetExamDetailResponse? = response.body()
                    Log.d(
                        "examdetailresponseTAG",
                        "Status: ${examDetail?.status} + dataSize: ${examDetail?.data!!.size}"
                    )
                }
            }

            override fun onFailure(call: Call<GetExamDetailResponse>, t: Throwable) {
                Log.d("examdetailresponseTAG", "Failure, message: ${t.message}")
            }

        })
    }

    // ✅ Updated signature to accept List<SubjectMark>
    private fun setupSubjectRecyclerView(list: List<SubjectMark>) {
        if (list.isEmpty()) {
            binding.rvSubjectsScore.visibility = View.GONE
            binding.llMarks.visibility = View.GONE
            binding.llNoDataFound.visibility = View.VISIBLE
        }
        subjectAdapter = SubjectAdapter(list, this@ReportCardActivity)
        binding.rvSubjectsScore.apply {
            layoutManager = LinearLayoutManager(this@ReportCardActivity)
            adapter = subjectAdapter
        }
    }


    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSearch.setOnClickListener {
            Log.d("btnsearchxxx", PrefsManager.getUserDetailedInformation(applicationContext).studentData.get(0).studentId)
            Log.d("btnsearchxxx", "sidinc: " + PrefsManager.getUserDetailedInformation(applicationContext).studentData.get(0).sidInc)
            val selectedExamName = (binding.spinnerExam as? AutoCompleteTextView)?.text.toString()

            val selectedExam = exams.firstOrNull { it.name == selectedExamName }
            Log.d("btnsearchxxx", "selected exam id: " + selectedExam?.id)

            if (selectedExam == null) {
                Snackbar.make(
                    binding.root,
                    "Please select a valid exam from the list.",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            maxMarksToSent = selectedExam.maxMarks.toIntOrNull() ?: 100

            fetchStudentMarks(selectedExam.id)
        }
    }

    private fun applyAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.cardExamSelection.startAnimation(fadeIn)
    }

    private fun showError(message: String) {
        binding.textError.text = message
        binding.textError.visibility = View.VISIBLE
    }

    private fun fetchStudentMarks(examId: String) {
        binding.textError.visibility = View.GONE
        binding.containerLoading.visibility = View.VISIBLE
        binding.containerResult.visibility = View.GONE
        binding.llNoDataFound.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                delay(1500)

                val userDetails = PrefsManager.getUserDetailedInformation(applicationContext)
                val scholarId = userDetails.studentData.get(0).sidInc
                val s = GetMarksRequest(scholarId.toInt(), examId.toInt())

                ApiClient.getMarksInstance.getMarks(
                    "application/json",
                    "BEARER_TOKEN",
                    s
                ).enqueue(object : retrofit2.Callback<GetMarksResponse> {

                    override fun onResponse(
                        call: Call<GetMarksResponse>,
                        response: Response<GetMarksResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val s: GetMarksResponse? = response.body()
                            Log.d(
                                "marksresponseTAG",
                                "Status: ${s?.status} + dataSize: ${s?.data}"
                            )
                            binding.containerLoading.visibility = View.GONE

                            // ✅ Now check the marks list inside the data object
                            if (s?.data?.marks?.isNotEmpty() == true) {
                                // ✅ Pass the correct data object to displayResult
                                displayResult(s.data)
                                // ✅ Pass the correct marks list to setupSubjectRecyclerView
                                setupSubjectRecyclerView(s.data.marks)

                                binding.containerLoading.visibility = View.GONE
                                binding.containerResult.visibility = View.VISIBLE
                                binding.llNoDataFound.visibility = View.GONE
                                binding.rvSubjectsScore.visibility = View.VISIBLE
                                binding.llMarks.visibility = View.VISIBLE
                            } else {
                                binding.containerLoading.visibility = View.GONE
                                binding.containerResult.visibility = View.VISIBLE
                                binding.llNoDataFound.visibility = View.VISIBLE
                                binding.rvSubjectsScore.visibility = View.GONE
                                binding.llMarks.visibility = View.GONE
                                binding.textStudentName.text = ""
                                binding.textScholarId.text = ""
                                binding.textExamName.text = ""
                            }
                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(call: Call<GetMarksResponse>, t: Throwable) {
                        Log.d("marksresponseTAG", "Failure, message: ${t.message}")
                    }
                })

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.containerLoading.visibility = View.GONE
                    Log.d("marksresponseTAG", "Failure, message: ${e.message}")
                    showError("Failed to fetch results. Please try again.")
                }
            }
        }
    }

    private fun getExamId(examName: String): String {
        if ("mid ter exams".equals(examName)) {
            for (i in 0 until exams.size) {
                if (examName.equals(exams[i].name)) {
                    maxMarksToSent = exams[i].maxMarks.toInt()
                    break
                }
            }
            return "1"
        }
        if ("Unit Test 1".equals(examName)) {
            return "2"
        }
        return "-1"
    }

    // ✅ Updated signature to accept the MarksData object
    private fun displayResult(result: MarksData) {
        // Hide loading
        binding.containerLoading.visibility = View.GONE

        // Set student info
        binding.textStudentName.text = result.studentName
        binding.textScholarId.text = "Scholar ID: ${result.scholar}"
        // The exam name is not in the MarksData, so you will need to get it elsewhere
        // For example, from the selected Exam object
        // binding.textExamName.text = selectedExamName

        // Set summary using the total marks from the API
        binding.textMarksObtained.text = result.totalObtainedMarks.toString()
        binding.textTotalMarks.text = result.totalMaxMarks.toString()

        val obtainedMarks = result.totalObtainedMarks.toFloat()
        val totalMarks = result.totalMaxMarks.toFloat()

        val percentage = (obtainedMarks / totalMarks) * 100

        Log.d("percentageTAG", percentage.toString())
        binding.textPercentage.text = percentage.toString()
        binding.textPercentage.setTextColor(getGradeColor(percentage))

        binding.containerResult.visibility = View.VISIBLE
        binding.containerResult.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.slide_up)
        )

        animateCounter(0f, percentage, binding.textPercentage)
    }

    // ✅ The calculateObtainedMarks function is no longer needed since the API provides the total
    // You can remove this function entirely

    private fun animateCounter(start: Float, end: Float, textView: View) {
        val animator = ValueAnimator.ofFloat(start, end)
        animator.duration = 1500
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            if (textView == binding.textPercentage) {
                binding.textPercentage.text = "%.1f%%".format(value)
            }
        }
        animator.start()
    }

    private fun getGradeColor(percentage: Float): Int {
        return when {
            percentage >= 90 -> ContextCompat.getColor(this, R.color.grade_a)
            percentage >= 80 -> ContextCompat.getColor(this, R.color.grade_b)
            percentage >= 70 -> ContextCompat.getColor(this, R.color.grade_c)
            percentage >= 60 -> ContextCompat.getColor(this, R.color.grade_d)
            else -> ContextCompat.getColor(this, R.color.grade_f)
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

    data class Exam(val id: String, val name: String, val maxMarks: String)
}