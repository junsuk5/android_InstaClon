package com.example.instaclon


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_create_post.*
import kotlinx.coroutines.launch


class CreatePostFragment : Fragment() {
    private val imageUri = MutableLiveData<Uri>()

    private val viewModel by viewModels<CreatePostViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isProgress.observe(this, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

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
            uploadPost()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun uploadPost() {
        imageUri.value?.let { uri ->

            val stream = requireContext().contentResolver.openInputStream(uri)

            lifecycleScope.launch {
                viewModel.uploadPostAsync(stream!!, editText.text.toString())
                requireActivity().finish()
            }
        }
    }

    companion object {
        const val REQUEST_IMAGE_GET = 1
    }


}
