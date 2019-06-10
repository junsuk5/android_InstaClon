package com.example.instaclon


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_create_post.*


class CreatePostFragment : Fragment() {
    private val imageUri = MutableLiveData<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageUri.observe(this, Observer { uri ->
            Glide.with(imageView)
                .load(uri)
                .into(imageView)
        })

        camera_button.setOnClickListener {
            selectImage()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_IMAGE_GET
            && resultCode == Activity.RESULT_OK
            && intent != null
        ) {
            imageUri.value = intent.data!!
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.create_post, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_send) {
            // 이미지 업로드
            imageUri.value?.let {
                val downloadUri = uploadImage(it).task.result
                Log.d("Log", "${downloadUri.toString()}")
            }

            // 업로드 결과를 DB에 작성
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun uploadImage(uri: Uri): UploadTask.TaskSnapshot {
        val stream = requireContext().contentResolver.openInputStream(uri)

        val ref = FirebaseStorage.getInstance().reference
            .child("images/${System.currentTimeMillis()}.jpg")
        return Tasks.await(ref.putStream(stream!!))
    }

    companion object {
        const val REQUEST_IMAGE_GET = 1
    }


}
