package com.example.instaclon


import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeImageTransform
import com.example.instaclon.databinding.FragmentPostDetailBinding
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.coroutines.launch


class PostDetailFragment : Fragment() {
    private val args: PostDetailActivityArgs by navArgs()
    private val viewModel by viewModels<CreatePostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = ChangeImageTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val binding = FragmentPostDetailBinding.bind(view)
        binding.viewModel = viewModel
        binding.post = args.post
        binding.lifecycleOwner = this

        description_edit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val post = args.post
                post.description = description_edit.text.toString()

                lifecycleScope.launch {
                    viewModel.updatePost(post)
                    requireActivity().finish()
                }

                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.post_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                lifecycleScope.launch {
                    viewModel.deletePost(args.post)
                    requireActivity().finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
