package fr.delcey.fizzbuzz.ui.parameters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.delcey.fizzbuzz.ViewModelFactory
import fr.delcey.fizzbuzz.databinding.FragmentParametersBinding

class ParametersFragment : Fragment() {

    private var _binding: FragmentParametersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParametersBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this, ViewModelFactory).get(ParametersViewModel::class.java)

        binding.parametersButtonConfirm.setOnClickListener {
            viewModel.onButtonConfirmClicked(
                binding.parametersTextInputEditTextInt1.text.toString(),
                binding.parametersTextInputEditTextInt2.text.toString(),
                binding.parametersTextInputEditTextLimit.text.toString(),
                binding.parametersTextInputEditTextStr1.text.toString(),
                binding.parametersTextInputEditTextStr2.text.toString()
            )
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) {
            binding.parametersTextInputEditTextInt1.setTextKeepState(it.int1)
            binding.parametersTextInputEditTextInt2.setTextKeepState(it.int2)
            binding.parametersTextInputEditTextLimit.setTextKeepState(it.limit)
            binding.parametersTextInputEditTextStr1.setTextKeepState(it.str1)
            binding.parametersTextInputEditTextStr2.setTextKeepState(it.str2)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}