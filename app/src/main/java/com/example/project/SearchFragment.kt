package com.example.project

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.FragmentSearchBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SearchFragment : Fragment() {
    private var firestore : FirebaseFirestore? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("test")
    private var adapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        val recyclerview = binding.FragrecyclerView

        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(emptyList())
        recyclerview.adapter = adapter
        firestore = FirebaseFirestore.getInstance()

        //updateList()

        binding.search.setOnClickListener {
            queryWhere(binding)
        }

        return binding.root
    }

    private fun queryWhere(binding: FragmentSearchBinding) {
        val p = binding.searchWord.text.toString()
        itemsCollectionRef.whereEqualTo("name", p).get()
            .addOnSuccessListener {
                val items = arrayListOf<String>()
                val results = mutableListOf<MyItem>()
                for (doc in it) {
                    items.add("${doc["name"]} - ${doc["email"]}")
                    results.add(MyItem(doc))
                }
                adapter?.updateList(results)
                binding.result.text = items.toString()
            }
            .addOnFailureListener {
            }
    }
    private fun updateList() {
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<MyItem>()
            for (doc in it) {
                items.add(MyItem(doc))
            }
            adapter?.updateList(items)
        }
    }
    companion object {

    }
}