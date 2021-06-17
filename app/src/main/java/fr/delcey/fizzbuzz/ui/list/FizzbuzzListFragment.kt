package fr.delcey.fizzbuzz.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.delcey.fizzbuzz.ViewModelFactory
import fr.delcey.fizzbuzz.databinding.FragmentFizzbuzzListBinding

class FizzbuzzListFragment() : Fragment() {

    private var _binding: FragmentFizzbuzzListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFizzbuzzListBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this, ViewModelFactory).get(FizzbuzzListViewModel::class.java)

        val adapter = FizzbuzzAdapter()
        binding.fizzbuzzRecyclerView.adapter = adapter

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) {
            adapter.items = it
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}