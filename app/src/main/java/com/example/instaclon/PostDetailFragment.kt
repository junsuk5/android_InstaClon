package com.example.instaclon


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Explode
import androidx.transition.TransitionInflater
import com.example.instaclon.databinding.FragmentPostDetailBinding
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PostDetailFragment : Fragment() {
    private val args: PostDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CreatePostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = Explode()
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPostDetailBinding.bind(view)
        binding.viewModel = viewModel
        binding.post = args.post
        binding.lifecycleOwner = this

        description_edit.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val post = args.post
                post.description = description_edit.text.toString()

                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.updatePost(post) {
                        findNavController().popBackStack()
                    }
                }

                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
    }


}
