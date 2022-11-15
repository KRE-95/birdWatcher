package com.gruppe16.birdwatcher.components

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.gruppe16.birdwatcher.data.Listing
import com.gruppe16.birdwatcher.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseDatabase {

    private val listingCollectionRef = Firebase.firestore.collection("listings")
    private val userCollectionRef = Firebase.firestore.collection("users")


    private var _pictureUrl : String? = null
    val pictureUrl: String?
        get() = _pictureUrl

    protected fun saveUser(user: User)  = CoroutineScope(Dispatchers.IO).launch {
        try {
            userCollectionRef.add(user)
            withContext(Dispatchers.Main) {
                //Toast.makeText(this@HomeFragment.requireContext(), "Saved data", Toast.LENGTH_LONG).show()
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                //Toast.makeText(this@HomeFragment.requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveListing(listing: Listing, fragment: Fragment)  = CoroutineScope(Dispatchers.IO).launch {
        try {
            listingCollectionRef.add(listing)
            withContext(Dispatchers.Main) {
                Toast.makeText(fragment.requireContext(), "Saved data", Toast.LENGTH_LONG).show()
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(fragment.requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun uploadPhotoToStorage(id: String, selectedPhotoUri: Uri?) {
        setPictureUrl(null)
        val storage = FirebaseStorage.getInstance().getReference("/photos/$id")
        storage.putFile(selectedPhotoUri!!)
            .addOnCompleteListener() {
                Log.d("CREATE", "Successfully uploaded photo.")

                storage.downloadUrl.addOnSuccessListener {
                    Log.d("CREATE", "URL: $it")
                    setPictureUrl(it.toString())
                    Log.d("CREATE", "PICTURE URL $pictureUrl")
                }
            }
    }

    fun getDataFromFirestore(adapter: Adapter): ArrayList<Listing>{

        val db = FirebaseFirestore.getInstance()
        val list = ArrayList<Listing>()
        db.collection("listings")//TODO Add query to sort by descending?
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(ContentValues.TAG, document.id + " => " + document.data)

                        //For hvert leste objekt legges det til i lokal lagring
                        val newObject = document.toObject(Listing::class.java)
                        list.add(newObject)
                    }
                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", task.exception)
                }
            }
        return list
    }

    fun deletePhotoFromStorage(id: String) {
        val storage = FirebaseStorage.getInstance().getReference("/photos/$id")
        storage.delete().addOnSuccessListener {
            Log.d("DELETE", "Picture deleted from storage")
        }
    }

    private fun setPictureUrl(url: String?){
        _pictureUrl = url
    }
}