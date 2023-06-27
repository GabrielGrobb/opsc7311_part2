package com.example.opsc7311_part2

//Add necessary references for view
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7311_part2.databinding.ActivityAddActivityBinding
import com.example.opsc7311_part2.databinding.ActivityAddCategoryBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.app.DatePickerDialog
import android.media.Image
import android.text.AutoText
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Spinner
import java.time.Duration
import android.widget.Toast
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.opengl.Visibility
import android.os.Build
import android.os.Environment
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit



class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddActivityBinding

    private var activityCounter = ToolBox.ActivityManager.getActivityList().size
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val STORAGE_PERMISSION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }
        //Views
        val ActivityIcon: ImageView = findViewById(R.id.ActivityIcon)
        val tilLocation: TextInputLayout = findViewById(R.id.til_Location)
        val tilCategory: TextInputLayout = findViewById(R.id.til_Category)
        val txtCategory = findViewById<Spinner>(R.id.spCategory)
        //calendars
        val tilStartDate: TextInputLayout = findViewById(R.id.til_StartDate)
        val tilEndDate: TextInputLayout = findViewById(R.id.til_EndDate)
        val imgActivityIcon: ImageView = findViewById(R.id.ActivityIcon)
        //input text boxes
        val txtTitle: TextInputEditText = findViewById(R.id.txtTitle)
        val txtClient: TextInputEditText = findViewById(R.id.txtClient)
        val txtLocation: TextInputEditText = findViewById(R.id.txtLocation)
        val spCategory: Spinner = findViewById(R.id.spCategory)
        val txtDuration: TextInputEditText = findViewById(R.id.txtDuration)
        val txtStartDate: TextInputEditText = findViewById(R.id.txtStartDate)
        val txtEndDate: TextInputEditText = findViewById(R.id.txtEndDate)
        //label text vies
        val txtTitleError: TextView = findViewById(R.id.txtTitleError)
        val txtClientError: TextView = findViewById(R.id.txtClientError)
        val txtLocationError: TextView = findViewById(R.id.txtLocationError)
        val txtCategoryError : TextView = findViewById(R.id.txtCategoryError)
        val txtDurationError : TextView = findViewById(R.id.txtDurationError)
        val txtEndDateError : TextView = findViewById(R.id.txtEndDateError)
        //adapter
        val categoryAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getCategoryNames())
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = categoryAdapter
        //getting a list of all the categories
        val categoryList = ToolBox.CategoryManager.getCategoryList()
        //adding activity
        binding.btnAddActivity.setOnClickListener {

            // Increment the categoryCounter
            activityCounter++

            val actTitle = txtTitle.text.toString()
            val actClient = txtClient.text.toString()
            val actLocation = txtLocation.text.toString()
            val actDuration = txtLocation.text.toString()
            val actEndDate = findViewById<TextInputEditText>(R.id.txtEndDate).text.toString()
            val actStartDate = findViewById<TextInputEditText>(R.id.txtStartDate).text.toString()
            val activityImg = findViewById<ImageView>(R.id.ActivityIcon)
            val bitmap: Bitmap? = (activityImg.drawable as? BitmapDrawable)?.bitmap
            var isValid = false
            var allInputsValid = 0
            if(spCategory.selectedItem != null){
                val selectedCategory = spCategory.selectedItem.toString()
                val category = categoryList.find { it.name == selectedCategory }
                val categoryId = category?.catID ?: -1 // Default value if category not found
                //validating activity Title
                isValid = Validation().validateStringsWithNumbers(actTitle)
                if (!isValid){
                    txtTitleError.text = "The title can only contain letters and numbers!"
                    txtTitleError.visibility = View.VISIBLE
                    allInputsValid += 1
                }
                //ensuring activity title is unique
                isValid = ToolBox.ActivityManager.findActivityByName(actTitle)
                if(isValid == true){
                    txtTitleError.text = "Another activity exists with the same name!"
                    txtTitleError.visibility = View.VISIBLE
                    allInputsValid += 1
                }
                //validating activity Client
                isValid = Validation().validateStringsNoNumbers(actClient)
                if(!isValid){
                    txtClientError.text = "The client can only contain letters!"
                    txtClientError.visibility = View.VISIBLE
                    allInputsValid += 1
                }
                //validating activity location
                isValid = Validation().validateStringsWithNumbers(actLocation)
                if(!isValid){
                    txtLocationError.text = "The location can only contain letters and numbers!"
                    txtLocation.visibility = View.VISIBLE
                    allInputsValid +=1
                }
                //validating end date
                isValid = Validation().isEndDateAfterStartDate(actStartDate,actEndDate)
                if(!isValid){
                    txtEndDateError.text = "The End date must be after the start date!"
                    txtEndDateError.visibility = View.VISIBLE
                    allInputsValid+=1
                }
                //ensuring all inputs are valid
                if(allInputsValid == 0){
                    //setting all error text views back to invisible
                    txtTitleError.visibility = View.INVISIBLE
                    txtClientError.visibility = View.INVISIBLE
                    txtLocationError.visibility = View.INVISIBLE
                    txtCategoryError.visibility = View.INVISIBLE
                    txtDurationError.visibility = View.INVISIBLE
                    txtEndDateError.visibility = View.INVISIBLE
                    //Creating the activity object
                    val newActivity = ToolBox.ActivityDataClass(
                        activityCounter,
                        txtTitle.text.toString(),
                        txtClient.text.toString(),
                        txtLocation.text.toString(),
                        selectedCategory,
                        categoryId,
                        Duration.ofHours(txtDuration.text.toString().toLong()),
                        ProgressionBar(),
                        Duration.ZERO,
                        Duration.ZERO,
                        actStartDate.toString(),
                        actEndDate.toString(),
                        bitmap
                    )
                    //adding the activity object to the activity data class
                    //Testing DB Integrations
                    //ToolBox.DBManager.persistActivity(newActivity)
                    ToolBox.ActivityManager.addActivity(newActivity)

                    category?.activities?.add(newActivity)
                    // Return to the HomePage
                    val intent = Intent(this, HomePageTest::class.java)
                    startActivity(intent)
                }
            }else{
                txtCategoryError.text = "A Category must first be added in the homepage!"
                txtCategoryError.visibility = View.VISIBLE
            }
        }

        //Listeners
        tilLocation.setEndIconOnClickListener(){

        }

        tilCategory.setEndIconOnClickListener(){

        }

        tilStartDate.setEndIconOnClickListener {
            showDatePickerDialog(txtStartDate)
        }

        tilEndDate.setEndIconOnClickListener(){
            showDatePickerDialog(txtEndDate)
        }

        ActivityIcon.setOnClickListener {

            showYesNoDialog()
        }


    }

    private fun getCategoryNames(): List<String> {
        val categoryList = ToolBox.CategoryManager.getCategoryList()
        return categoryList.map { it.name }
    }


    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
                if (photo != null) {
                    val activityIcon: ImageView = findViewById(R.id.ActivityIcon)
                    activityIcon.setImageBitmap(photo)

                    // Save the captured photo as the Activity Icon
                    saveActivityIcon(photo)
                }
            }
        }
    private fun showYesNoDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to open the camera?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                requestCameraPermission()
            }
            .setNegativeButton("No") { dialog, id ->
                showToast("No clicked!")
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Confirmation")
        alert.show()
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                showToast("Camera permission denied!")
            }
        }
    }

    private fun getActivityIcon(): Uri? {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val profilePhotoUriString = sharedPreferences.getString("profilePhotoUri", null)
        return profilePhotoUriString?.let { Uri.parse(it) }
    }

    private fun saveActivityIcon(bitmap: Bitmap) {
        val fileName = generateFileName()
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val imageUri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageToMediaStoreQ(contentResolver, bitmap, fileName)
        } else {
            saveImageToMediaStore(contentResolver, bitmap, fileName)
        }

        if (imageUri != null) {
            // Save the Uri of the activity icon in storage
            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("activityIconUri", imageUri.toString())
            editor.apply()

            showToast("Activity icon saved successfully!")
        } else {
            showToast("Failed to save activity icon!")
        }
    }

    private fun generateFileName(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "ProfilePhoto_$timeStamp.jpg"
    }

    private fun saveImageToMediaStoreQ(
        contentResolver: ContentResolver,
        photo: Bitmap,
        fileName: String
    ): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val imageUri = contentResolver.insert(collection, contentValues)

        if (imageUri != null) {
            try {
                contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(imageUri, contentValues, null, null)
                return imageUri
            } catch (e: IOException) {
                contentResolver.delete(imageUri, null, null)
            }
        }

        return null
    }


    // Save the profile photo to MediaStore for API levels < 29
    private fun saveImageToMediaStore(
        contentResolver: ContentResolver,
        photo: Bitmap,
        fileName: String
    ): Uri? {
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File(imagesDir, fileName)

        try {
            FileOutputStream(imageFile).use { outputStream ->
                photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            }

            return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } catch (e: IOException) {
            imageFile.delete()
        }

        return null
    }

    // Convert a Bitmap to a Uri
    private fun bitmapToUri(bitmap: Bitmap): Uri {
        val cachePath = File(applicationContext.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "tempProfilePhoto.png")

        try {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.fromFile(file)
    }

    private fun showDatePickerDialog(textField: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the text field with the selected date
                val formattedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                textField.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}