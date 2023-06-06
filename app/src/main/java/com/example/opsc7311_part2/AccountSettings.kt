package com.example.opsc7311_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.opsc7311_part2.databinding.ActivityAccountSettingsBinding
import com.google.android.material.navigation.NavigationView
import android.widget.ImageView
import android.widget.Toast
import android.app.AlertDialog
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import android.graphics.Bitmap
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.content.Context
import android.content.ContentResolver
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText




class AccountSettings : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityAccountSettingsBinding
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val STORAGE_PERMISSION_REQUEST_CODE = 101
    private var profilePicture: Bitmap? = null
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Getting a reference to the views to Update Account settings
        val minHours = findViewById<Spinner>(R.id.min_time)
        val maxHours = findViewById<Spinner>(R.id.max_time)
        //Creating the items for the spinner
        val minHoursItems = arrayOf(1,2,3,4,5,6,7,8)
        val maxHoursItems = arrayOf(1,2,4,5,6,7,8,9,10,11,12)
        //Creating the Adapters for the spinners
        val minHoursAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, minHoursItems)
        val maxHoursAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, maxHoursItems)
        //Setting the adapters for the spinners
        minHours.adapter = minHoursAdapter
        maxHours.adapter = maxHoursAdapter

        val email = findViewById<TextInputEditText>(R.id.txtEmail)
        val username = findViewById<TextInputEditText>(R.id.txtUsername)
        val firstName = findViewById<TextInputEditText>(R.id.txtFirstname)
        val surname = findViewById<TextInputEditText>(R.id.txtSurname)
        val password = findViewById<TextInputEditText>(R.id.txtPassword)
        val updateButton = findViewById<Button>(R.id.updateSettings)

        //Retrieves the current settings object from toolbox to be updated
        var currentSettings = ToolBox.AccountManager.getSettingsObject()

        setSupportActionBar(binding.navToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var toggleOnOff = ActionBarDrawerToggle(
            this,
            binding.drawerLayout, binding.navToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggleOnOff)
        toggleOnOff.syncState()

        binding.navView.bringToFront()
        binding.navView.setNavigationItemSelectedListener(this)

        val minSpinner: Spinner = findViewById(R.id.min_time)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.min_time_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            minSpinner.adapter = adapter
        }

        val maxSpinner: Spinner = findViewById(R.id.max_time)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.max_time_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            maxSpinner.adapter = adapter
        }

        //To Make Profile Picture Clickable

        val profilePicture: ImageView = findViewById(R.id.profilePicture)
        val profilePhotoUri = getProfilePhotoUri()
        if (profilePhotoUri != null) {
            profilePicture.setImageURI(profilePhotoUri)
        }
        profilePicture.setOnClickListener {

            showOptionDialog()
            showToast("You Need To Allow Access To Your Camera")
        }
        //Setting the different views to the user's account settings
        fun updateAccountSettings(){
            //Finding the index of the desired min and max hours in the spinner to set the view
            val desiredMin = ToolBox.CategoryManager.getSpinnerIndexForValue(minHours, currentSettings.minHours.toString())
            val desiredMax = ToolBox.CategoryManager.getSpinnerIndexForValue(maxHours, currentSettings.maxHours.toString())
            //Setting the spinners
            minHours.setSelection(desiredMin)
            maxHours.setSelection(desiredMax)
            email.setText(currentSettings.email)
            username.setText(currentSettings.username)
            firstName.setText(currentSettings.firstName)
            surname.setText(currentSettings.surname)
            password.setText(currentSettings.password)
            println(currentSettings.minHours)
        }

        //Setting the account settings to visually match the users desired settings
        updateAccountSettings()

        //Code to handle user updating settings
        updateButton.setOnClickListener{
            //Setting all of the values of the currentSettings object with data user has entered
            currentSettings.updateSettings(
                "default",
                //Getting the currently selected item from the spinner, casting to string then int
                minHours.selectedItem.toString().toInt(),
                maxHours.selectedItem.toString().toInt(),
                email.text.toString(),
                username.text.toString(),
                firstName.text.toString(),
                surname.text.toString(),
                password.text.toString()
            )
            updateAccountSettings()
        }


    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
                if (photo != null) {
                    val profilePicture: ImageView = findViewById(R.id.profilePicture)
                    profilePicture.setImageBitmap(photo)

                    // Save the captured photo as the profile photo
                    saveProfilePhoto(photo)
                }
            }
        }


    private fun showOptionDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to allow access to your camera?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                requestCameraPermission()
            }
            .setNegativeButton("No") { dialog, id ->
                showToast("No Camera Permission!")
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


    /*private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        } else {
            // Check if the profilePicture variable is already initialized
            if (profilePicture != null) {
                saveProfilePhoto(profilePicture!!)
            } else {
                showToast("No profile picture selected!")
            }
        }
    }*/

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }




    // Get the Uri of the profile photo from storage
    private fun getProfilePhotoUri(): Uri? {
        // Retrieve the profile photo Uri from storage if available
        // Return the Uri or null if not available
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val profilePhotoUriString = sharedPreferences.getString("profilePhotoUri", null)
        return profilePhotoUriString?.let { Uri.parse(it) }
    }

    private fun saveProfilePhoto(photo: Bitmap) {
        val fileName = generateFileName()
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val imageUri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageToMediaStoreQ(contentResolver, photo, fileName)
        } else {
            saveImageToMediaStore(contentResolver, photo, fileName)
        }

        if (imageUri != null) {
            // Save the Uri of the profile photo in storage
            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("profilePhotoUri", imageUri.toString())
            editor.apply()

            showToast("Profile photo saved successfully!")
        } else {
            showToast("Failed to save profile photo!")
        }
    }

    // Generate a file name for the profile photo
    private fun generateFileName(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "ProfilePhoto_$timeStamp.jpg"
    }


    //--------------------------------------------------------------------------------------------//
    /*For devices with API level 29 (Android Q) and above, the image is saved using the MediaStore
    API.
    The image is stored in the Pictures directory of the primary external storage.
    The saveImageToMediaStoreQ() function is responsible for saving the image to the MediaStore.

    For devices with API level below 29, the image is saved to the public Pictures directory in the
    external storage. The saveImageToMediaStore() function is responsible for saving the
    image in this case.

    Here are the default storage locations for both cases:

    For API level 29 and above:

    Directory: Environment.DIRECTORY_PICTURES
    Location: Primary external storage (usually the user's public Pictures directory)
    For API level below 29:

    Directory: Environment.DIRECTORY_PICTURES
    Location: Public Pictures directory in the external storage*/

    //--------------------------------------------------------------------------------------------//


    // Save the profile photo to MediaStore for API levels >= 29 (Android Q)
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

                //............................................................................................//






                //............................................................................................//

                override fun onNavigationItemSelected(item: MenuItem): Boolean {

                    when (item.itemId) {
                        R.id.nav_home -> {
                            val intent = Intent(applicationContext, HomePageTest::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                        R.id.nav_schedule -> {
                            val intent = Intent(applicationContext, Schedule::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)

                        }

                        R.id.nav_achievements -> {
                            val intent = Intent(applicationContext, AchievementActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)

                        }

                        R.id.nav_logout -> {
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    // return true marks the item as selected
                    return true
                }

                //............................................................................................//

                override fun onBackPressed() {
                    //if the drawer is open, close it
                    if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        //otherwise, let the super class handle it
                        super.onBackPressed()
                    }
                }

                //............................................................................................//

                override fun onClick(v: View?) {
                    /*TODO("Not yet implemented")*/
                }

                //............................................................................................//

            }

//.........................................EndOfFile..............................................//