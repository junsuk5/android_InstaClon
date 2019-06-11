package com.example.instaclon

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instaclon.models.Post
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.InputStream

class CreatePostViewModel : ViewModel() {
    val isProgress = MutableLiveData<Boolean>()

    init {
        isProgress.value = false
    }

    fun uploadImage(stream: InputStream): Uri {
        isProgress.postValue(true)
        val ref = FirebaseStorage.getInstance().reference
            .child("images/${System.currentTimeMillis()}.jpg")

        return Tasks.await(ref.putStream(stream).continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            isProgress.postValue(false)
            return@Continuation ref.downloadUrl
        }))
    }

    fun createPost(post: Post): DocumentReference {
        isProgress.postValue(true)
        val db = FirebaseFirestore.getInstance()
        return Tasks.await(db.collection("insta_posts").add(post).addOnCompleteListener {
            isProgress.postValue(false)
        })
    }
}