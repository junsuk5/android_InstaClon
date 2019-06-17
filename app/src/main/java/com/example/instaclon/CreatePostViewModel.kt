package com.example.instaclon

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instaclon.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.InputStream

class CreatePostViewModel : ViewModel() {
    val db = FirebaseFirestore.getInstance()

    val isProgress = MutableLiveData<Boolean>()

    init {
        isProgress.value = false
    }

    suspend fun uploadPostAsync(stream: InputStream, text: String) {
        isProgress.postValue(true)
        val ref = FirebaseStorage.getInstance().reference
            .child("images/${System.currentTimeMillis()}.jpg")

        ref.putStream(stream).await()

        val downloadUri = ref.downloadUrl.await()

        val post = Post(
            "a811219@gmail.com",
            FirebaseAuth.getInstance().currentUser?.email,
            FirebaseAuth.getInstance().currentUser?.photoUrl?.toString(),
            downloadUri.toString(),
            text
        )

        db.collection("insta_posts").add(post).await()

        isProgress.postValue(false)
    }

    suspend fun updatePost(post: Post) {
        isProgress.postValue(true)
        db.collection("insta_posts").document(post.uid).set(post).await()
        isProgress.postValue(false)
    }

    fun deletePost(post: Post, callback: () -> Unit) {
        isProgress.postValue(true)

        db.collection("insta_posts").document(post.uid).delete()
            .addOnCompleteListener {
                callback.invoke()
            }
    }
}