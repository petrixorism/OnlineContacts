package uz.gita.onlinecontacts.ui.screen

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding

import uz.gita.onlinecontacts.R
import uz.gita.onlinecontacts.databinding.MainScreenBinding
import uz.gita.onlinecontacts.ui.viewmodel.MainViewModel

class MainScreen : Fragment(R.layout.main_screen) {

    private val binding: MainScreenBinding by viewBinding(MainScreenBinding::bind)
    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val nextButton = binding.nextButton

        viewModel.loggedLiveData.observe(viewLifecycleOwner) {
            if (it) {
                nextButton.visibility = VISIBLE

            } else {
                binding.view.visibility = VISIBLE
                binding.loginButton.visibility = VISIBLE
                binding.registerButton.visibility = VISIBLE
            }
        }

        nextButton.setOnClickListener {
            findNavController().navigate(MainScreenDirections.actionMainFragmentToHomeScreen())
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_loginScreen)
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_registerScreen)
        }

    }
}