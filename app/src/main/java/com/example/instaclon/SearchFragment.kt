package com.example.instaclon


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclon.databinding.ItemPostBinding
import com.example.instaclon.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search.*
import androidx.core.util.Pair as UtilPair


class SearchFragment : Fragment() {
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseFirestore.getInstance()
            .collection("insta_posts")

        val options = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .build()

        adapter = PostAdapter(options) { view, post ->
            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                UtilPair.create(view, "image")
            )
            val extras = ActivityNavigatorExtras(activityOptions)
            val action = SearchFragmentDirections.actionSearchFragmentToPostDetailActivity(post)
            findNavController().navigate(action, extras)
        }

        recycler_view.adapter = adapter

        create_button.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_createPostActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    class PostAdapter(
        options: FirestoreRecyclerOptions<Post>,
        private val callback: (View, Post) -> Unit
    ) :
        FirestoreRecyclerAdapter<Post, PostAdapter.PostHolder>(options) {

        class PostHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_post, parent, false)
            return PostHolder(ItemPostBinding.bind(view))
        }

        override fun onBindViewHolder(holder: PostHolder, position: Int, model: Post) {

            model.uid = snapshots.getSnapshot(position).id
            holder.binding.post = model
            holder.binding.root.setOnClickListener {
                callback.invoke(holder.binding.imageView, model)
            }
        }

    }


}


