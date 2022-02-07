package uz.gita.onlinecontacts.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.onlinecontacts.R
import uz.gita.onlinecontacts.app.App
import uz.gita.onlinecontacts.data.auth_request.NamePasswordData
import uz.gita.onlinecontacts.data.auth_request.NamePhoneData
import uz.gita.onlinecontacts.data.response.ContactResponse
import uz.gita.onlinecontacts.databinding.HomeScreenBinding
import uz.gita.onlinecontacts.ui.adapters.ContactAdapter
import uz.gita.onlinecontacts.ui.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior


class HomeScreen : Fragment(R.layout.home_screen) {

    private val binding by viewBinding(HomeScreenBinding::bind)
    private val viewModel: HomeViewModel by viewModels()
    private val adapter = ContactAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        viewModel.getAllContactLiveData.observe(viewLifecycleOwner, contactListObserver)
        viewModel.notConnectionLiveData.observe(viewLifecycleOwner, notConnectionObserver)
        viewModel.failLiveData.observe(viewLifecycleOwner, failObserver)
        viewModel.notifyChangeLiveData.observe(viewLifecycleOwner, changeObserver)
        viewModel.userExitLiveData.observe(viewLifecycleOwner, exitObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)

        viewModel.getContactList()

//        val list = ArrayList<ContactResponse>()
//        list.add(ContactResponse(0, "qwewr", "64613153"))
//        adapter.submitList(list)

        adapter.setMoreButtonListener {
            showPopUp(it.button, it.data)
        }

        binding.settingButton.setOnClickListener {
            showDialog()

        }

        binding.addContactButton.setOnClickListener {
            showBottomsheet()
        }


    }

    //--- Pop up menu ---//

    fun showPopUp(button: Button, data: ContactResponse) {
        val popup = PopupMenu(requireContext(), button)
        popup.inflate(R.menu.item_menu)
        popup.show()
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete_contact -> {
                    viewModel.deleteContact(data.id)
                    return@setOnMenuItemClickListener true
                }
                R.id.edit_contact -> {
                    editBottomSheet(data)
                    return@setOnMenuItemClickListener true
                }
            }

            return@setOnMenuItemClickListener false
        }
    }

    //--- Create dialog ---//
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.setting_dialog)

        val logoutButton = dialog.findViewById<Button>(R.id.logout_button)
        val delAccButton = dialog.findViewById<Button>(R.id.delete_account_button)

        logoutButton.clicks()
            .debounce(1000L)
            .onEach {
                dialog.dismiss()
                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle("Log out")
                //set message for alert dialog
                builder.setMessage("Do you want to log out?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes") { _, _ ->
                    viewModel.logout()
                    dialog.dismiss()
                }
                //performing cancel action
                builder.setNeutralButton("Cancel") { dialogInterface, which ->
                    dialogInterface.dismiss()
                }

                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(true)
                alertDialog.show()


            }.launchIn(lifecycleScope)

        delAccButton.clicks()
            .debounce(1000L)
            .onEach {

                val builder = AlertDialog.Builder(requireContext())
                //set title for alert dialog
                builder.setTitle("Delete account")
                //set message for alert dialog
                builder.setMessage("All your data will be deleted")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteAccount()
                    dialog.dismiss()
                }
                //performing cancel action
                builder.setNeutralButton("Cancel") { dialogInterface, which ->
                    dialogInterface.dismiss()
                }

                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(true)
                alertDialog.show()

            }.launchIn(lifecycleScope)

        dialog.show()


    }



    //--- Bottomsheet dialog ---//

    fun showBottomsheet() {
        val dialog = BottomSheetDialog(this.requireContext(), R.style.DialogStyle)
        val view = layoutInflater.inflate(R.layout.bottomsheet, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()

        val addButton = dialog.findViewById<Button>(R.id.add_contact_button)
        val name = dialog.findViewById<EditText>(R.id.contact_name)
        val phone = dialog.findViewById<EditText>(R.id.contact_phone)

        addButton!!.clicks()
            .debounce(500L)
            .onEach {
                viewModel.addContact(
                    NamePhoneData(
                        name!!.text.toString(),
                        phone!!.text.toString()
                    )
                )
                dialog.dismiss()
            }.launchIn(lifecycleScope)
    }

    @SuppressLint("InflateParams")
    fun editBottomSheet(data: ContactResponse) {
        val dialog = BottomSheetDialog(this.requireContext(), R.style.DialogStyle)
        val view = layoutInflater.inflate(R.layout.bottomsheet, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()

        val addButton = dialog.findViewById<Button>(R.id.add_contact_button)
        val name = dialog.findViewById<EditText>(R.id.contact_name)
        val phone = dialog.findViewById<EditText>(R.id.contact_phone)

        name!!.setText(data.name)
        phone!!.setText(data.phone)
        addButton!!.clicks()
            .debounce(500L)
            .onEach {
                viewModel.editContact(
                    ContactResponse(
                        data.id,
                        name!!.text.toString(),
                        phone!!.text.toString()
                    )
                )
                dialog.dismiss()
            }.launchIn(lifecycleScope)
    }


    //---  Observers  ---//

    private val contactListObserver = Observer<List<ContactResponse>> {
        adapter.submitList(it)
    }

    private val failObserver = Observer<String> {
        Toast.makeText(requireContext(), "fail", Toast.LENGTH_SHORT).show()
    }

    private val notConnectionObserver = Observer<String> {
        Toast.makeText(requireContext(), "progress", Toast.LENGTH_SHORT).show()
    }

    private val changeObserver = Observer<Unit> {
        viewModel.getContactList()
    }

    private val exitObserver = Observer<Unit> {
        Toast.makeText(requireContext(), "User exit", Toast.LENGTH_SHORT).show()
        findNavController().navigate(HomeScreenDirections.actionHomeScreenToMainFragment())

    }

    private val progressObserver = Observer<Boolean> {
        if (it) Toast.makeText(requireContext(), "progress", Toast.LENGTH_SHORT).show()
    }

}