package com.example.opsc7311_part2

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Spinner
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.collections.HashMap
import android.util.Base64
import com.example.opsc7311_part2.ToolBox.CategoryManager.getActivitiesForCategory
import com.example.opsc7311_part2.ToolBox.CategoryManager.getCategoriesFromDB
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

import java.io.ByteArrayOutputStream

class ToolBox {
    //------------------Data Classes and Objects
    data class ActivityDataClass(
        val actID: Int,
        val title: String,
        val client: String,
        val location: String,
        val category: String,
        val categoryId: Int, // ID of the category
        val duration: Duration,
        //var progressionBar: ProgressionBar,
        var currentTimeSpent: Duration,
        var savedTimeSpent: Duration,
        val startDate: String,
        val endDate: String,
        var actImage: Bitmap?
    )


    data class CategoryDataClass(
        var catID: Int,
        var name: String,
        var activityTimeSpent: Duration,
        val catColor: Int
    )

    data class AccountSettings(
        var userImage: String,
        var minHours: Int,
        var maxHours: Int,
        var email: String,
        var username: String,
        var firstName: String,
        var surname: String,
        var password: String
    ) {
        fun updateSettings(
            userImage: String,
            minHours: Int,
            maxHours: Int,
            email: String,
            username: String,
            firstName: String,
            surname: String,
            password: String
        ) {
            this.userImage = userImage
            this.minHours = minHours
            this.maxHours = maxHours
            this.email = email
            this.username = username
            this.firstName = firstName
            this.surname = surname
            this.password = password
        }
    }

    object AccountManager {
        private val currentSettings = AccountSettings(
            "@drawable/default_profile",
            1,
            1,
            "default@default.com",
            "default",
            "default",
            "default",
            "default"
        )

        fun getSettingsObject(): AccountSettings {
            return currentSettings;
        }

    }

    object ActivityManager {

        fun addActivity(activity: ActivityDataClass) {
            //activityList
        }

        //Takes in an activityid and returns an activity object from the list if it exists
        fun getActivityObjectByID(id: Int): ActivityDataClass {
            var activityList = getActivityList()
            println("preloop")
            for (activity in activityList) {
                println("test")
                println(activity.toString())
                if (activity.actID == id) {
                    return activity
                }
            }
            //Again gonna break the code, love you gents <3
            return activityList[-1]
        }

        fun findActivityByName(name: String): Boolean {
            return getActivityList().any { it.title == name }
        }

        fun getActivityList(): MutableList<ActivityDataClass> {
            return DBManager.getActivitiesFromDB()
        }

    }

    //Reworked most backend methods to use DB instead of runtime memory, kept logic the same
    object DBManager {

        //Instance of DB
        val db = FirebaseFirestore.getInstance()

        //Gets the ID of the Firestore Document based off the activity ID, which is always unique
        fun getDocumentIDByTypeID(collectionName: String, fieldName: String, fieldValue: Any): String = runBlocking {
            var documentID: String?
            withContext(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val collectionRef = db.collection("Activities")
                val querySnapshot = collectionRef.whereEqualTo(fieldName, fieldValue).get().await()

                //documentID = querySnapshot.first().toString()
                //print(documentID)
                documentID = try {
                    querySnapshot.documents[0].id
                } catch (exception: IndexOutOfBoundsException) {
                    "none"
                }
            }
            documentID ?: "none"
        }

        //Function to update the current time spent on an activity
        fun updateActivityCurrentTime(actId: String, newTimeSpent: Duration) {
            val activitiesCollection = db.collection("Activities")
            val activityDocRef = activitiesCollection.document(getDocumentIDByTypeID("Activities", "actID", actId))

            activityDocRef
                .update("currentTimeSpent", newTimeSpent.toString())
                .addOnSuccessListener {
                    println("currentTimeSpent updated successfully")
                }
                .addOnFailureListener { e ->
                    println("Error updating currentTimeSpent: $e")
                }
        }

        fun updateActivitySavedTimeSpent(actId: String, newTimeSpent: Duration) {
            val activitiesCollection = db.collection("Activities")
            val activityDocRef = activitiesCollection.document(getDocumentIDByTypeID("Activities", "actID", actId))
            println(newTimeSpent)
            activityDocRef
                .update("savedTimeSpent", newTimeSpent.toString())
                .addOnSuccessListener {
                    println("currentTimeSpent updated successfully")
                }
                .addOnFailureListener { e ->
                    println("Error updating currentTimeSpent: $e")
                }
        }

        /*Function to count the number of activities currently in the collection, and return an
        integer representing a unique ID*/
        fun getUniqueActID(): Int {
            val collectionRef = db.collection("Activities")
            var returnVal = 0
            collectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val documentCount = querySnapshot.size()
                    returnVal = documentCount
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occurred while retrieving the documents
                    println("Error getting documents: ${exception.message}")
                }
            return returnVal + 1;
        }

        fun encodeImageToBase64(imageBitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes = baos.toByteArray()
            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        }

        fun uploadImageToFirebaseStorage(imageBitmap: Bitmap, onComplete: (imageUrl: String?) -> Unit) {
            val storageReference = FirebaseStorage.getInstance().reference
            val imagesRef = storageReference.child("images")
            val fileName = UUID.randomUUID().toString() + ".jpg"

            val imageRef = imagesRef.child(fileName)
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()

            val uploadTask = imageRef.putBytes(imageData)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result.toString()
                    onComplete(downloadUrl)
                } else {
                    onComplete(null)
                }
            }
        }

        //Functions to generate maps for the POCOs
        fun getActivityAttributes(activity: ActivityDataClass, imagePath: Bitmap): HashMap<String, Any> {
            val attributeMap = HashMap<String, Any>()

            attributeMap["actID"] = activity.actID
            attributeMap["title"] = activity.title
            attributeMap["client"] = activity.client
            attributeMap["location"] = activity.location
            attributeMap["category"] = activity.category
            attributeMap["categoryId"] = activity.categoryId
            attributeMap["duration"] = activity.duration.toString()
            attributeMap["currentTimeSpent"] = activity.currentTimeSpent.toString()
            attributeMap["savedTimeSpent"] = activity.savedTimeSpent.toString()
            attributeMap["startDate"] = activity.startDate
            attributeMap["endDate"] = activity.endDate

            // Encode the image and add it to the attribute map
            val encodedImage = encodeImageToBase64(imagePath)
            attributeMap["actImage"] = encodedImage

            return attributeMap
        }

        /*fun getActivitiesFromDB(): MutableList<ActivityDataClass> = runBlocking {
            val activityListDeferred = async(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val result = db.collection("Activities").get().await()
                val activityList = mutableListOf<ActivityDataClass>()
                for (document in result) {
                    val temp = ActivityDataClass(
                        document.data["actID"].toString().toInt(),
                        document.data["title"].toString(),
                        document.data["client"].toString(),
                        document.data["location"].toString(),
                        document.data["category"].toString(),
                        document.data["categoryId"].toString().toInt(),
                        Duration.parse(document.data["duration"].toString()),
                        Duration.parse(document.data["currentTimeSpent"].toString()),
                        Duration.parse(document.data["savedTimeSpent"].toString()),
                        document.data["startDate"].toString(),
                        document.data["endDate"].toString()
                    )
                    activityList.add(temp)
                }
                activityList
            }

            // Wait for the activityListDeferred to complete and return the result
            activityListDeferred.await()
        }*/

        fun getActivitiesFromDB(): MutableList<ActivityDataClass> = runBlocking {
            val activityListDeferred = async(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val result = db.collection("Activities").get().await()
                val activityList = mutableListOf<ActivityDataClass>()
                for (document in result) {
                    val temp = ActivityDataClass(
                        document.data["actID"].toString().toInt(),
                        document.data["title"].toString(),
                        document.data["client"].toString(),
                        document.data["location"].toString(),
                        document.data["category"].toString(),
                        document.data["categoryId"].toString().toInt(),
                        Duration.parse(document.data["duration"].toString()),
                        Duration.parse(document.data["currentTimeSpent"].toString()),
                        Duration.parse(document.data["savedTimeSpent"].toString()),
                        document.data["startDate"].toString(),
                        document.data["endDate"].toString(),
                        null
                    )

                    // Check if the document contains the "actImage" field
                    if (document.contains("actImage")) {
                        val encodedImage = document.data["actImage"].toString()
                        // Decode the Base64 encoded image to Bitmap
                        val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        temp.actImage = bitmap // Set the actImageBitmap property
                    }

                    activityList.add(temp)
                }
                activityList
            }

            // Wait for the activityListDeferred to complete and return the result
            activityListDeferred.await()
        }

        fun stringToBitmap(encodedString: String): Bitmap? {
            try {
                // Decode the Base64 string to a byte array
                val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)

                // Create a Bitmap from the byte array
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                return bitmap
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        //Takes in an ActivityDataClass, converts it to a hashmap and adds it to the database
       /* fun persistActivity(activity: ActivityDataClass) {
            // Get a reference to the collection
            val collectionRef = db.collection("Activities")

            // Get the attribute map with the encoded image
            val attributeMap = getActivityAttributes(activity)

            db.collection("Activities").add(attributeMap)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }*/
        fun persistActivity(activity: ActivityDataClass) {
            val db = FirebaseFirestore.getInstance()
            val collectionRef = db.collection("Activities")

            val tempActImage = activity.actImage // Temporary variable

            val encodedImage = if (tempActImage != null) {
                encodeImageToBase64(tempActImage)
            } else {
                null // Set the encoded image to null if the Bitmap is null
            }

            // Create a map of activity attributes including the image URL
            val attributeMap = hashMapOf<String, Any?>(
                "actID" to activity.actID,
                "title" to activity.title,
                "client" to activity.client,
                "location" to activity.location,
                "category" to activity.category,
                "categoryId" to activity.categoryId,
                "duration" to activity.duration.toString(),
                "currentTimeSpent" to activity.currentTimeSpent.toString(),
                "savedTimeSpent" to activity.savedTimeSpent.toString(),
                "startDate" to activity.startDate,
                "endDate" to activity.endDate,
                "actImage" to encodedImage
            )

            // Add the activity attributes to the database
            collectionRef.add(attributeMap)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }

    }

    object CategoryManager {
        private val categoryList = mutableListOf<CategoryDataClass>()
        //private val activityList = mutableListOf<ActivityDataClass>()

        //Returns a mutable list of activity data class given a catID
        fun getActivitiesForCategory(catID: String): MutableList<ActivityDataClass> = runBlocking {
            val activityListDeferred = async(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val collectionRef = db.collection("Activities")
                val querySnapshot = collectionRef.whereEqualTo("categoryId", catID).get().await()
                var activities = mutableListOf<ActivityDataClass>()
                for (document in querySnapshot) {
                    val temp = ActivityDataClass(
                        document.data["actID"].toString().toInt(),
                        document.data["title"].toString(),
                        document.data["client"].toString(),
                        document.data["location"].toString(),
                        document.data["category"].toString(),
                        document.data["categoryId"].toString().toInt(),
                        Duration.parse(document.data["duration"].toString()),
                        Duration.parse(document.data["currentTimeSpent"].toString()),
                        Duration.parse(document.data["savedTimeSpent"].toString()),
                        document.data["startDate"].toString(),
                        document.data["endDate"].toString(),
                        null
                    )

                    // Check if the document contains the "actImage" field
                    if (document.contains("actImage")) {
                        val encodedImage = document.data["actImage"].toString()
                        // Decode the Base64 encoded image to Bitmap
                        val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
                        val bitmap =
                            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        temp.actImage = bitmap // Set the actImageBitmap property
                    }
                    activities.add(temp)
                }
                activities
            }
            activityListDeferred.await()

        }

        //Takes in a category object and returns a duration representing the total amount of
        //Time spent on that category
        fun calcCategoryTime(cat: CategoryDataClass): Duration {
            var totalDuration = Duration.ZERO
            for (activity in getActivitiesForCategory(cat.catID.toString())) {
                //if(cat.catID==activity.categoryId) {
                totalDuration = totalDuration.plus(activity.currentTimeSpent)
                //}

            }
            return totalDuration
        }

        fun getCategoriesFromDB(): MutableList<CategoryDataClass> = runBlocking {
            val categoryListDeferred = async(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val result = db.collection("Category").get().await()
                val categoryList = mutableListOf<CategoryDataClass>()
                for (document in result) {
                    var temp = CategoryDataClass(
                        document.data["catID"].toString().toInt(),
                        document.data["name"].toString(),
                        Duration.parse(document.data["activityTimeSpent"].toString()),
                        document.data["catColour"].toString().toInt()
                    )
                    categoryList.add(temp)
                }
                categoryList
            }
            categoryListDeferred.await()
        }


        //Takes in a category id and returns the category object from the list if it exists
        fun getCategoryByID(id: Int): CategoryDataClass {
            for (category in getCategoryList()) {
                if (category.catID == id) {
                    return category
                }
            }
            //Not smart enough to fix will crash app sorry gents
            return getCategoryList()[-1]
        }

        fun addCategory(category: CategoryDataClass) {
            categoryList.add(category)
        }

        /* fun addActivity(activity: ActivityDataClass){
             activityList.add(activity)
         }*/

        fun getCategoryList(): List<CategoryDataClass> {
            for(category in getCategoriesFromDB()){println(category.toString())}
            return getCategoriesFromDB()
        }

        //--------General Utility Functions

        //Returns the current Date as a String
        fun getCurrentDateString(): String {
            val TimeCalendar = Calendar.getInstance().time
            //Creating the Format for the date so the page will just show the date without time or timezone info
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(TimeCalendar.time)
            return formattedDate
        }

        //Takes in a spinner and an int and returns the index of the int in the spinner if it exists
        fun getSpinnerIndexForValue(spinner: Spinner, value: String): Int {
            val adapter = spinner.adapter

            for (index in 0 until adapter.count) {
                val item = adapter.getItem(index)
                if (item.equals(value)) {
                    return index
                }
            }

            return -1 // Return -1 if the desired value is not found
        }

        //Takes in an activity object and returns a percentage value as an integer related to how far through the max length the activity is
        fun calcPercentageTimeSpent(activity: ActivityDataClass): Int {
            val totalTime = activity.duration.toMillis() // Total duration in milliseconds
            val currentTimeSpent =
                activity.currentTimeSpent.toMillis() // Current time spent in milliseconds

            if (totalTime > 0) {
                val percentage = (currentTimeSpent.toFloat() / totalTime.toFloat()) * 100
                return percentage.toInt()
            }

            // Return 0 if the total time is zero (to avoid division by zero)
            return 0
        }


        fun parseDateString(dateString: String): Date {
            val format = SimpleDateFormat("yyyy-MM-dd")
            return format.parse(dateString)
        }

        fun getActivitiesForCategoryBetweenDates(
            cat: CategoryDataClass,
            date1: Date,
            date2: Date
        ): List<ActivityDataClass> {
            val workingList = mutableListOf<ActivityDataClass>()
            for (activity in getActivitiesForCategory(cat.catID.toString())) {
                if (parseDateString(activity.startDate) >= date1 && parseDateString(activity.endDate) <= date2) {
                    workingList.add(activity)
                }
            }
            return workingList
        }

        //Takes in two dates and calculates the amount of time spent working between said dates
        fun sumTotalWorkingTime(workingList: List<ActivityDataClass>): Int {
            var totalTime = 0
            for (activity in workingList) {
                totalTime += activity.savedTimeSpent.toHours().toInt()
            }
            return totalTime
        }
        //Hi ishmael if you are reading this your feet smell ps gabe lifts more than you


    }
}
