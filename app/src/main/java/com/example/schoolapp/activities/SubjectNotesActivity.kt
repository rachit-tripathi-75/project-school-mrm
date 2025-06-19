package com.example.schoolapp.activities

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.schoolapp.R
import com.example.schoolapp.activities.HomeWorkActivity
import com.example.schoolapp.adapters.SubjectAdapter
import com.example.schoolapp.adapters.SubjectNotesAdapter
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.Constants.Companion.APS_DOWNLOAD_NOTIFICATION_CHANNEL_ID
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.ActivitySubjectNotesBinding
import com.example.schoolapp.fragments.ActivityDetailFragment
import com.example.schoolapp.models.SubjectNotesModel
import com.example.schoolapp.networks.NetworkChangeReceiver
import com.example.schoolapp.responses.HomeworkResponse
import com.example.schoolapp.responses.SubjectNote
import com.example.schoolapp.responses.SubjectNotesResponse
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class SubjectNotesActivity : AppCompatActivity() {


    var networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
        override fun onNetworkConnected() {
            binding.llNoInternetFound.visibility = View.GONE
            binding.coordinatorLayoutMainContent.visibility = View.VISIBLE
            initialisers()
            listeners()
            setupRecyclerView()
            setupRepository()
            loadSubjectNotes()
            Log.d("networkInterceptorTAG", "inside onNetworkConnected()")

        }

        override fun onNetworkDisconnected() {
            binding.coordinatorLayoutMainContent.visibility = View.GONE
            binding.llNoInternetFound.visibility = View.VISIBLE
            Log.d("networkInterceptorTAG", "inside onNetworkDisconnected()")
            Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_LONG).show()
        }
    })


    private lateinit var binding: ActivitySubjectNotesBinding
    private lateinit var subjectNotesAdapter: SubjectNotesAdapter
    private var path: String = ""
    private val STORAGE_PERMISSION_CODE = 101
//    private lateinit var homeworkRepository: SubjectRepository

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySubjectNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()
        listeners()
        setupRecyclerView()
        setupRepository()
        loadSubjectNotes()
    }

    private fun initialisers() {
        binding.tvSectionIdAndYear.text = "${getCurrentYear()} • Section ${PrefsManager.getSectionId(this)}"
    }

    private fun getCurrentYear(): Int {
        return java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    }




    private fun listeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setupRecyclerView() {
        subjectNotesAdapter = SubjectNotesAdapter { homework, action ->
            when (action) {
//                SubjectNotesAdapter.Action.VIEW -> viewPdf(homework)
                SubjectNotesAdapter.Action.VIEW -> Log.d("viewingPDF", "viewPDF")
//                SubjectNotesAdapter.Action.DOWNLOAD -> downloadPdf(homework)
                SubjectNotesAdapter.Action.DOWNLOAD -> {
                    if (checkAndRequestStoragePermission(this, this)) {
                        fetchFile(this, path, homework.fileNote)
                    }
                }
            }
        }

        binding.recyclerViewSubjectNotes.apply {
            layoutManager = LinearLayoutManager(this@SubjectNotesActivity)
            adapter = subjectNotesAdapter
        }
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAndRequestStoragePermission(context: Context, activity: Activity): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                STORAGE_PERMISSION_CODE
            )
            return false
        }
        return true
    }





    private fun fetchFile(context: Context, path: String, fileName: String) {
        createNotificationChannel(context)

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var progressNotification = createDownloadProgressNotification(context, 0)
        notificationManager.notify(1, progressNotification)

        Thread {
            try {
                val call = ApiClient.downloadPdfInstance.downloadPdf(fileName)
                val response = call.execute()

                if (response.isSuccessful && response.body() != null) {
                    val totalFileSize = response.body()!!.contentLength()

                    val uri = saveFileToScopedStorage(context, fileName)
                    if (uri == null) throw Exception("Unable to create file Uri")

                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        response.body()!!.byteStream().use { inputStream ->
                            val buffer = ByteArray(8192)
                            var bytesRead: Int
                            var totalBytesRead: Long = 0

                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                outputStream.write(buffer, 0, bytesRead)
                                totalBytesRead += bytesRead

                                val progress = ((totalBytesRead * 100) / totalFileSize).toInt()

                                Handler(Looper.getMainLooper()).post {
                                    progressNotification = createDownloadProgressNotification(context, progress)
                                    notificationManager.notify(1, progressNotification)
                                }
                            }

                            outputStream.flush()
                        }
                    }

                    // Mark file as not pending
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Downloads.IS_PENDING, 0)
                    }
                    context.contentResolver.update(uri, contentValues, null, null)

                    Handler(Looper.getMainLooper()).post {
                        val finishedNotification = createDownloadFinishedNotification(context, uri)
                        notificationManager.notify(1, finishedNotification)
                    }

                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    val finishedNotification = createDownloadFinishedNotification(context, null)
                    notificationManager.notify(1, finishedNotification)
                    Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }



    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                APS_DOWNLOAD_NOTIFICATION_CHANNEL_ID,
                "Downloads",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for file download progress"
            }

            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createDownloadProgressNotification(context: Context, progress: Int): Notification {
        return NotificationCompat.Builder(context, APS_DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Downloading PDF...")
            .setContentText("Progress: $progress%")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(100, progress, false)
            .setOngoing(true)
            .build()
    }

    fun createDownloadFinishedNotification(context: Context, uri: Uri?): Notification {
        Log.d("urivalue", "uri:" + uri.toString())

//        val openIntent = Intent(Intent.ACTION_VIEW).apply {
//            setDataAndType(uri, "application/pdf")
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
//        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            openIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )

        return NotificationCompat.Builder(context, APS_DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Download Finished")
            .setContentText("Tap to view the PDF")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
            .build()

    }

    fun saveFileToScopedStorage(context: Context, fileName: String): Uri? {
        return try {
            val resolver = context.contentResolver

            val mimeType = when {
                fileName.endsWith(".pdf", ignoreCase = true) -> "application/pdf"
                fileName.endsWith(".jpg", ignoreCase = true) || fileName.endsWith(".jpeg", ignoreCase = true) -> "image/jpeg"
                fileName.endsWith(".png", ignoreCase = true) -> "image/png"
                else -> "application/octet-stream"
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val uri = resolver.insert(collection, contentValues)

            uri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }




    private fun setupRepository() {
//        homeworkRepository = HomeworkRepository()
    }

    private fun loadSubjectNotes() {
        showLoading(true)

        lifecycleScope.launch {
            try {
                delay(1500)


                ApiClient.subjectNotesInstance.getSubjectNotes(
                    "Bearer TOKEN required.......",
                    "application/x-www-form-urlencoded",
                    "ci_session=r5p9d5d8b41l8fn13msacrh7n4ff9sm4",
                    PrefsManager.getSectionId(applicationContext)).enqueue(object: retrofit2.Callback<SubjectNotesResponse> {

                    override fun onResponse(call: Call<SubjectNotesResponse?>, response: Response<SubjectNotesResponse?>) {
                        showLoading(false)
                        if (response.isSuccessful && response.body() != null) {
                            val subjectNotes = response.body()
                            if (subjectNotes != null && subjectNotes.status == 1 && subjectNotes.data.isNotEmpty()) {
                                binding.llNoDataFound.visibility = View.GONE
                                path = subjectNotes.path
                                setDataToRecyclerView(subjectNotes.data)
                            } else {
                                binding.llNoDataFound.visibility = View.VISIBLE
                                binding.coordinatorLayoutMainContent.visibility = View.GONE
//                                Toast.makeText(this@SubjectNotesActivity, "No notes found", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            showError("Something went wrong!")
                        }

                    }

                    override fun onFailure(call: Call<SubjectNotesResponse?>, t: Throwable) {
//                        binding.clAllContent.visibility = View.GONE
//                        binding.progressBar.visibility = View.GONE
//                        binding.llInternalServerError.visibility = View.VISIBLE
                        binding.coordinatorLayoutMainContent.visibility = View.GONE
                        binding.llInternalServerError.visibility = View.VISIBLE
                        Log.d("subjectNotesTAG", "${t.message}")
                    }

                })
            } catch (e: Exception) {
                Log.d("subjectNotesTAG", "${e.message}")
                Toast.makeText(this@SubjectNotesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDataToRecyclerView(data: List<SubjectNote>) {

        val list = mutableListOf<SubjectNotesModel>()
        list.clear()
//        val list = SubjectNotesModel()

        for (i in 0 until data.size) {
            list.add(SubjectNotesModel(data.get(i).id, data.get(i).sstId, data.get(i).secId, data.get(i).subId, data.get(i).subName, data.get(i).fileNote, data.get(i).sessionId, "", "", data.get(i).createdOn))
        }

        subjectNotesAdapter.setData(list)
    }

//    private fun viewPdf(homework: Homework) {
//        lifecycleScope.launch {
//            try {
//                showLoading(true)
//
//                val pdfFile = withContext(Dispatchers.IO) {
//                    homeworkRepository.downloadPdfFile(homework.fileNote)
//                }
//
//                showLoading(false)
//                openPdfFile(pdfFile)
//
//            } catch (e: Exception) {
//                showLoading(false)
//                showError("Failed to open PDF: ${e.message}")
//            }
//        }
//    }

//    private fun downloadPdf(homework: Homework) {
//        lifecycleScope.launch {
//            try {
//                showLoading(true)
//
//                val success = withContext(Dispatchers.IO) {
//                    homeworkRepository.downloadPdfToDownloads(homework.fileNote, homework.subject)
//                }
//
//                showLoading(false)
//
//                if (success) {
//                    Toast.makeText(this@HomeworkActivity, "PDF downloaded successfully", Toast.LENGTH_SHORT).show()
//                } else {
//                    showError("Failed to download PDF")
//                }
//
//            } catch (e: Exception) {
//                showLoading(false)
//                showError("Download failed: ${e.message}")
//            }
//        }
//    }

    private fun openPdfFile(file: File) {
        try {
            val uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                showError("No PDF viewer app found")
            }

        } catch (e: Exception) {
            showError("Failed to open PDF: ${e.message}")
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show()
                // You can now access/download the PDF
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
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