package fr.delcey.fizzbuzz.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.delcey.fizzbuzz.ViewModelFactory
import fr.delcey.fizzbuzz.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this, ViewModelFactory).get(StatsViewModel::class.java)

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) {
            binding.statsTextViewDescription.text = it
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}