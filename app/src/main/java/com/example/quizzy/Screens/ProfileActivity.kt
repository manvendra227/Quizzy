package com.example.quizzy.Screens

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzy.Adapter.UserAttemptsAdapter
import com.example.quizzy.R
import com.example.quizzy.dataModel.model.SavedUserModel
import com.example.quizzy.databinding.ActivityProfileBinding
import com.example.quizzy.databinding.PopupChangePasswordBinding
import com.example.quizzy.databinding.PopupDeleteUserBinding
import com.example.quizzy.utilities.LoginStateSharedPrefrence
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.utilities.UserDetailsSharedPrefrence
import com.example.quizzy.viewModels.ProfileViewModel
import com.example.quizzy.viewModels.ViewModelFactory.ProfileViewModelFactory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.list_item_user_attempts.*


class ProfileActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewModelFactory: ProfileViewModelFactory
    private lateinit var adapter: UserAttemptsAdapter
    private lateinit var userId: String

    private lateinit var dialogBinding1: PopupChangePasswordBinding
    private lateinit var dialog1: Dialog

    private lateinit var dialogBinding2: PopupDeleteUserBinding
    private lateinit var dialog2: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        userId = intent.getStringExtra("userId").toString()

        val savedUserResponse = UserDetailsSharedPrefrence().getUserDetails(this)
        val savedUserModel = Gson().fromJson(savedUserResponse, SavedUserModel::class.java)

        if (userId == savedUserModel.userId) {
            binding.popMenu.visibility = View.VISIBLE
        }
        initViewModel(userId)
        initRecycler()
        observers()
        clicks()
        expandableLayout()
    }

    private fun initViewModel(userId: String?) {
        viewModelFactory = ProfileViewModelFactory(userId.toString())
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        binding.profileViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initRecycler() {
        binding.userAttemptRecycler.layoutManager = LinearLayoutManager(this)
        adapter = UserAttemptsAdapter(this)
        binding.userAttemptRecycler.adapter = adapter
    }

    private fun observers() {

        viewModel.getUserAttemptList().observe(this) {
            if (it != null) {
                adapter.setAttemptList(it)
                adapter.notifyDataSetChanged()
            } else {
                Log.i("profileActivity", "Error")
            }
        }

        viewModel.deleteStatus.observe(this){
            if (it){
                UserDetailsSharedPrefrence().clearUserDetails(this)
                LoginStateSharedPrefrence().clearState(this)
                startActivity(Intent(this,LoginActivity::class.java))
                finishAffinity()
            }else{
                MyToast(this).showShort("Oops! Something wet wrong")
            }
        }
        viewModel.errorMessageDeleteUser.observe(this){
            dialogBinding2.password.error=viewModel.errorMessageDeleteUser.value
        }
        viewModel.errorMessageChangePasswordOld.observe(this){
            dialogBinding1.oldPassword.error=viewModel.errorMessageChangePasswordOld.value
        }
        viewModel.errorMessageChangePasswordNew.observe(this){
            dialogBinding1.newPassword.error=viewModel.errorMessageChangePasswordNew.value
        }
        viewModel.changePasswordStatus.observe(this){
            if (it) {
                MyToast(this).styleToast(this,"Password changed successfully")
                hideKeyboard()
                dialog1.dismiss()
            }
            else MyToast(this).showShort("Something went wrong")
        }
    }

    private fun clicks() {
        binding.emailButton.setOnClickListener {

            val email = Intent(Intent.ACTION_SEND)
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf(viewModel.emailID.value.toString()))
            email.putExtra(Intent.EXTRA_SUBJECT, "Subject Text Here..")
            email.putExtra(Intent.EXTRA_TEXT, "")
            email.type = "message/rfc822"
            startActivity(Intent.createChooser(email, "Send Mail Using :"))
        }

        binding.uploadButton.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("key", userId)
            startActivity(intent)
        }

        binding.popMenu.setOnClickListener {
            val view: ImageButton = findViewById(R.id.popMenu)
            popupMenu(view)
        }
    }

    private fun expandableLayout() {
        binding.userAttemptCard.setOnClickListener {

            if (binding.userAttemptRecycler.visibility == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(binding.userAttemptCard, AutoTransition())
                binding.userAttemptRecycler.visibility = View.GONE
                binding.arrow1.setImageResource(R.drawable.ic_arrow_close)
            } else {
                TransitionManager.beginDelayedTransition(binding.userAttemptCard, AutoTransition())
                binding.userAttemptRecycler.visibility = View.VISIBLE
                binding.arrow1.setImageResource(R.drawable.ic_arrow_down)
            }
        }

    }

    private fun popupMenu(view: View) {

        val wrapper: Context = ContextThemeWrapper(this, R.style.popupMenuStyle)
        val menu = PopupMenu(wrapper, view)
        menu.setOnMenuItemClickListener(this)
        menu.inflate(R.menu.profile_menu)
        menu.show()
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {

        return when (p0?.itemId) {
            R.id.editProfile -> {
                MyToast(this).showShort("Feature not implemented yet")
                true
            }
            R.id.changePassword -> {
                dialogChangePassword()
                true
            }
            R.id.deleteAccount -> {
                dialogDeleteUser()
                true
            }
            R.id.SignOut -> {
                LoginStateSharedPrefrence().clearState(this)
                UserDetailsSharedPrefrence().clearUserDetails(this)
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
                true
            }
            R.id.wishlist -> {
                true
            }
            else -> false
        }
    }

    private fun dialogChangePassword() {

        dialogBinding1 = PopupChangePasswordBinding.inflate(layoutInflater)
        dialog1 = Dialog(this)
        dialog1.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog1.setContentView(dialogBinding1.root)
        dialog1.window?.attributes!!.windowAnimations = R.style.DialogAnimation

        dialogBinding1.button.setOnClickListener {
            dialog1.dismiss()
        }

        dialogBinding1.dialogViewModel = viewModel
        dialog1.show()
    }

    private fun dialogDeleteUser() {

        dialogBinding2 = PopupDeleteUserBinding.inflate(layoutInflater)
        dialog2 = Dialog(this)
        dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog2.setContentView(dialogBinding2.root)
        dialog2.window?.attributes!!.windowAnimations = R.style.DialogAnimation

        dialogBinding2.button.setOnClickListener {
            dialog2.dismiss()
        }

        dialogBinding2.dialogViewModel = viewModel
        dialog2.show()
    }

    private fun hideKeyboard() {
        dialogBinding1.oldPassword.clearFocus()
        dialogBinding1.newPassword.clearFocus()
        val `in` = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(dialogBinding1.oldPassword.windowToken, 0)
        `in`.hideSoftInputFromWindow(dialogBinding1.newPassword.windowToken, 0)
    }
}