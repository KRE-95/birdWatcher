package com.gruppe16.birdwatcher.components

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.gruppe16.birdwatcher.data.Listing


class FirebaseDatabase {

    private val listingCollectionRef = Firebase.firestore.collection("listings")

    private var _pictureUrl : String? = null
    val pictureUrl: String?
        get() = _pictureUrl

    private fun setPictureUrl(url: String?){
        _pictureUrl = url
    }

    fun saveListing(listing: Listing, fragment: Fragment) {
            listingCollectionRef.add(listing)
                .addOnSuccessListener {
                    Toast.makeText(
                        fragment.requireContext(),
                        "Saved listing",
                        Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        fragment.requireContext(),
                        "Save listing failed. Try again.",
                        Toast.LENGTH_SHORT)
                        .show()
                }
    }

    fun uploadPhotoToStorage(id: String, selectedPhotoUri: Uri?) {
        setPictureUrl(null)
        val storage = FirebaseStorage.getInstance().getReference("/photos/$id")
        storage.putFile(selectedPhotoUri!!)
            .addOnCompleteListener {
                Log.d("STORAGE", "Successfully uploaded photo.")

                storage.downloadUrl.addOnSuccessListener {
                    setPictureUrl(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("STORAGE", "Upload of picture failed.")
            }
    }

    fun updateListing(docuRef: String, toUpdate: String, newItem: String) {
        listingCollectionRef.document(docuRef).update(toUpdate, newItem)
            .addOnSuccessListener {
                Log.d("UPDATE", "Listing updated")
            }
            .addOnFailureListener {
                Log.d("UPDATE", "Listing update failed")
            }
    }

    fun deletePhotoFromStorage(id: String) {
        val storage = FirebaseStorage.getInstance().getReference("/photos/$id")
        storage.delete().addOnSuccessListener {
            Log.d("DELETE", "Picture deleted from storage")
        }
            .addOnFailureListener {
                Log.d("DELETE", "Delete from storage failed")
            }
    }

    fun deleteListing(id: String, fragment: Fragment) {
        listingCollectionRef.document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(
                    fragment.requireContext(),
                    "Deleted listing",
                    Toast.LENGTH_SHORT)
                    .show()
                Log.d("DELETE", "Listing deleted")
            }
            .addOnFailureListener {
                Toast.makeText(
                    fragment.requireContext(),
                    "Deleted listing failed, try again",
                    Toast.LENGTH_SHORT)
                    .show()
                Log.d("DELETE", "Delete listing failed")
            }
    }
}


