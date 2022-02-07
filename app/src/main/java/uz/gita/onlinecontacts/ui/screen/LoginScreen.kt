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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.onlinecontacts.R
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.response.AuthResponse
import uz.gita.onlinecontacts.databinding.LoginScreenBinding
import uz.gita.onlinecontacts.ui.viewmodel.LoginViewModel

class LoginScreen : Fragment(R.layout.login_screen) {

    private val binding by viewBinding(LoginScreenBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
        viewModel.loginLiveData.observe(viewLifecycleOwner, registerObserver)
        viewModel.failLiveData.observe(viewLifecycleOwner, failObserver)
        viewModel.notConnectionLiveData.observe(viewLifecycleOwner, noInternetObserver)


        binding.loginButton.clicks()
            .debounce(1000L)
            .onEach {
                viewModel.login(
                    NamePasswordData(
                        binding.nameEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    )
                )
            }.launchIn(lifecycleScope)

        binding.backButton.clicks()
            .onEach {
                findNavController().navigate(LoginScreenDirections.actionLoginScreenToMainFragment())
            }.launchIn(lifecycleScope)

        binding.toRegisterButton.clicks()
            .onEach {
                findNavController().navigate(LoginScreenDirections.actionLoginScreenToRegisterScreen())
            }.launchIn(lifecycleScope)

    }


    //----  Observers   ----//

    private val progressObserver = Observer<Boolean> {
        if (it) Toast.makeText(requireContext(), "Progress", Toast.LENGTH_SHORT).show()
    }

    private val registerObserver = Observer<AuthResponse> {
        findNavController().navigate(LoginScreenDirections.actionLoginScreenToHomeScreen())
    }

    private val noInternetObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

    private val failObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }
}