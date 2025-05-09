package com.example.schoolapp.activities

import android.animation.ValueAnimator
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
import com.example.schoolapp.databinding.ActivityReportCardBinding
import com.example.schoolapp.networks.NetworkChangeReceiver
import com.example.schoolapp.responses.GetExamDetailResponse
import com.example.schoolapp.responses.GetMarksResponse
import com.example.schoolapp.responses.StudentMarkData
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
    private var maxMarks: Int = 100
    private var maxMarksToSent: Int = 100

    // Mock data for demo
    private var exams: List<Exam> = mutableListOf<Exam>()


    var networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
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

        ApiClient.getExamDetailInstance.getExamDetail("Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
            "ci_session=5b6qicv26lfo48kmvgnfpmqgllo5c579").enqueue(object: retrofit2.Callback<GetExamDetailResponse> {

            override fun onResponse(call: Call<GetExamDetailResponse>, response: Response<GetExamDetailResponse>) {
                if (response.isSuccessful) {
                    val s: GetExamDetailResponse? = response.body()
                    exams = mutableListOf<Exam>()
                    s?.data?.forEach { examData ->
                        (exams as MutableList<Exam>).add(Exam(examData.id, examData.name, examData.max_mark))
                    }

                    val examNames = exams.map { it.name }
                    val adapter = ArrayAdapter(this@ReportCardActivity, android.R.layout.simple_spinner_dropdown_item, examNames)
                    (binding.spinnerExam as? AutoCompleteTextView)?.setAdapter(adapter)

                    Log.d("examdetailresponseTAG", "Status: ${s?.status} + dataSize: ${s?.data?.size}")
                } else {
                    val examDetail: GetExamDetailResponse? = response.body()
                    Log.d("examdetailresponseTAG", "Status: ${examDetail?.status} + dataSize: ${examDetail?.data!!.size}")
                }
            }

            override fun onFailure(call: Call<GetExamDetailResponse>, t: Throwable) {
                Log.d("examdetailresponseTAG", "Failure, message: ${t.message}")
            }

        })
    }

    private fun setupSubjectRecyclerView(list: List<StudentMarkData>) {
        if (list.isEmpty()) {
            binding.rvSubjectsScore.visibility = View.GONE
            binding.llMarks.visibility = View.GONE
            binding.llNoDataFound.visibility = View.VISIBLE
        }
        subjectAdapter = SubjectAdapter(list, this@ReportCardActivity, maxMarksToSent)
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
            val selectedExamPosition = exams.indexOfFirst {
                it.name == (binding.spinnerExam as? AutoCompleteTextView)?.text.toString()
            }


//            if (selectedExamPosition == -1 || scholarId.isEmpty()) {
//                showError("Please select an exam and enter scholar ID")
//                return@setOnClickListener
//            }


//            fetchStudentMarks(selectedExamPosition, scholarId)
            fetchStudentMarks()
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

    private fun fetchStudentMarks() {
        // Hide error if visible
        binding.textError.visibility = View.GONE

        // Show loading
        binding.containerLoading.visibility = View.VISIBLE
        binding.containerResult.visibility = View.GONE
        binding.llNoDataFound.visibility = View.GONE

//         Simulate API call
        CoroutineScope(Dispatchers.IO).launch {
            try {

                // Simulate network delay
                delay(1500)
                ApiClient.getMarksInstance.getMarks("Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
                    "ci_session=5b6qicv26lfo48kmvgnfpmqgllo5c579",
                    "1675",
                    getExamId(binding.spinnerExam.text.toString())).enqueue(object: retrofit2.Callback<GetMarksResponse> {


                    override fun onResponse(call: Call<GetMarksResponse>, response: Response<GetMarksResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            val s: GetMarksResponse? = response.body()
                            binding.containerLoading.visibility = View.GONE
                            if (s?.data!!.size != 0) {
                                displayResult(s.data)
                                setupSubjectRecyclerView(s.data)
                                binding.containerLoading.visibility = View.GONE
                                binding.containerResult.visibility = View.VISIBLE
                                binding.llNoDataFound.visibility = View.GONE
                                binding.rvSubjectsScore.visibility = View.VISIBLE
                                binding.llMarks.visibility = View.VISIBLE
                                Log.d("abcdeTAG", "if case")
                            } else {
                                binding.containerLoading.visibility = View.GONE
                                binding.containerResult.visibility = View.VISIBLE
                                binding.llNoDataFound.visibility = View.VISIBLE
                                binding.rvSubjectsScore.visibility = View.GONE
                                binding.llMarks.visibility = View.GONE
                                binding.textStudentName.text = ""
                                binding.textScholarId.text = ""
                                binding.textExamName.text = ""
                                Log.d("abcdeTAG", "else case")
                            }

                            Log.d("marksresponseTAG", "Status: ${s?.status} + dataSize: ${s?.data?.size}")
                        } else {
                            val examDetail: GetMarksResponse? = response.body()
                            Log.d("marksresponseTAG", "Status: ${examDetail?.status} + dataSize: ${examDetail?.data!!.size}")
                        }
                    }

                    override fun onFailure(call: Call<GetMarksResponse>, t: Throwable) {
                        Log.d("marksresponseTAG", "Failure, message: ${t.message}")
                    }

                })


            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.containerLoading.visibility = View.GONE
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

    private fun displayResult(result: List<StudentMarkData>) {
        if (result.isEmpty()) {
            Log.d("reachedTAG", "result is empty")
            return
        }
        // Hide loading
        binding.containerLoading.visibility = View.GONE

        // Set student infoq
        binding.textStudentName.text = result[0].StudName
        binding.textScholarId.text = "Scholar ID: ${result[0].scholar}"
        binding.textExamName.text = result[0].ExamName

        // Set grade with appropriate color
//        binding.textGrade.text = result.grade
//        binding.textGrade.setTextColor(getGradeColor(result.))


        // Set summary
        binding.textMarksObtained.text = calculateObtainedMarks(result)
        binding.textTotalMarks.text = (result.size * maxMarksToSent).toString()
        val obtainedMarks = calculateObtainedMarks(result).toFloat()
        val totalMarks = result.size * maxMarksToSent
        val percentage = (obtainedMarks / totalMarks) * 100

        Log.d("percentageTAG", percentage.toString())
        binding.textPercentage.text = percentage.toString()
        binding.textPercentage.setTextColor(getGradeColor(percentage))

        // Show result with animation
        binding.containerResult.visibility = View.VISIBLE
        binding.containerResult.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.slide_up)
        )

//         Animate percentage counters
        animateCounter(0f, percentage, binding.textPercentage)
    }

    private fun String.div(totalMarks: Int): Float {
        val obtainedMarks: Int = this.toInt()
        return (obtainedMarks / totalMarks).toFloat()
    }

    private fun calculateObtainedMarks(result: List<StudentMarkData>): String {
        var marks = 0
        for (i in 0 until result.size) {
            marks+= result[i].mark.toInt()
        }
        return marks.toString()
    }

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
//    data class StudentResult(val scholarId: String, val studentName: String, val examId: String, val examName: String, val subject: List<SubjectMark>)



}