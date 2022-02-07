package uz.gita.onlinecontacts.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.onlinecontacts.R
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.response.AuthResponse
import uz.gita.onlinecontacts.databinding.RegisterScreenBinding
import uz.gita.onlinecontacts.ui.viewmodel.RegisterViewModel

class RegisterScreen : Fragment(R.layout.register_screen) {

    private val binding by viewBinding(RegisterScreenBinding::bind)
    private val viewModel: RegisterViewModel by viewModels()

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
        viewModel.registerLiveData.observe(viewLifecycleOwner, registerObserver)
        viewModel.failLiveData.observe(viewLifecycleOwner, failObserver)
        viewModel.notConnectionLiveData.observe(viewLifecycleOwner, noInternetObserver)

        binding.backButton.clicks()
            .onEach {
                findNavController().navigate(RegisterScreenDirections.actionRegisterScreenToMainFragment())
            }.launchIn(lifecycleScope)

        binding.toLogButton.clicks()
            .onEach {
                findNavController().navigate(RegisterScreenDirections.actionRegisterScreenToLoginScreen())
            }.launchIn(lifecycleScope)


        binding.registerButton.clicks()
            .debounce(500L)
            .onEach {
                viewModel.register(
                    NamePasswordData(
                        binding.nameEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    )
                )
            }.launchIn(lifecycleScope)

    }

    //----  Observers   ----//

    private val progressObserver = Observer<Boolean> {
        if (it) Toast.makeText(requireContext(), "Progress", Toast.LENGTH_SHORT).show()
    }

    private val registerObserver = Observer<AuthResponse> {
        findNavController().navigate(RegisterScreenDirections.actionRegisterScreenToHomeScreen())
    }

    private val noInternetObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

    private val failObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

}