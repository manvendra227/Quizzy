package com.example.quizzy.Screens

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.Adapter.HomePageAdapter
import com.example.quizzy.Adapter.SearchTagAdapter
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityHomeBinding
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.utilities.UserDetailsSharedPrefrence
import com.example.quizzy.viewModels.HomeViewModel
import com.example.quizzy.viewModels.ViewModelFactory.HomeViewModelFactory
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var homePageAdapter: HomePageAdapter
    private lateinit var searchTagAdapter: SearchTagAdapter
    private val emailSP = UserDetailsSharedPrefrence()
    private var toast = MyToast(this)
    private var doubleBackToExitPressedOnce = false


    var searchTag = MutableLiveData<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        initViewModel()
        initLoads()
        initRecyclerViewMain()
        initRecyclerViewTags()
        searchByTag()
        searchPressed()
        refreshQuiz()
        filterData()
        CreateButton()

    }


    private fun initRecyclerViewMain() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recycler_quiz.layoutManager = GridLayoutManager(this, 2)

        } else {
            recycler_quiz.layoutManager = LinearLayoutManager(this)
        }
        homePageAdapter = HomePageAdapter(this)
        recycler_quiz.adapter = homePageAdapter
    }

    private fun initRecyclerViewTags() {
        recycler_tags.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        searchTagAdapter = SearchTagAdapter(this)
        recycler_tags.adapter = searchTagAdapter
    }


    private fun initViewModel() {

        viewModelFactory = HomeViewModelFactory(emailSP.getEmailID(this).toString())
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        binding.homeViewModel = viewModel

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initLoads() {

        viewModel.getMainList().observe(this, Observer {
            if (it != null) {
                homePageAdapter.setQuizList(it)
                homePageAdapter.notifyDataSetChanged()
            } else {
                toast.showLong("Error")
            }
        })
        viewModel.getQuizData()


        viewModel.getTagsList().observe(this, Observer {
            if (it != null) {
                searchTagAdapter.setTagList(it)
                searchTagAdapter.notifyDataSetChanged()
            } else {
                toast.showLong("No tags found error")
            }
        })
        viewModel.getTagsData()
    }

    private fun searchPressed() {
        search_text.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFunction()
                return@OnEditorActionListener true
            }
            false
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchFunction() {
        viewModel.getSearchList().observe(this, Observer {
            if (it != null) {
                homePageAdapter.setQuizList(it)
                homePageAdapter.notifyDataSetChanged()
            } else {
                toast.showLong("Error")
            }
        })
        viewModel.getSearchData()
        hideKeyboard()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByTag() {
        searchTag.observe(this, Observer {
            toast.showShort(searchTag.value.toString())
            viewModel.getSearchList().observe(this, Observer {
                if (it != null) {
                    homePageAdapter.setQuizList(it)
                    homePageAdapter.notifyDataSetChanged()
                } else {
                    toast.showLong("Error hugeeee")
                }
            })
            viewModel.getSearchDataByTag(searchTag.value.toString())
        })
    }

    private fun refreshQuiz() {
        container.setOnRefreshListener {
            Handler().postDelayed(Runnable {
                container.isRefreshing = false
            }, 2000)
            initLoads()
        }
    }

    private fun filterData() {
        filter.setOnClickListener {
            val filterBox = LayoutInflater.from(this).inflate(R.layout.popup_search_filter, null)
            AlertDialog.Builder(this, R.style.Theme_Transparent).setView(filterBox).show()
        }
    }

    private fun hideKeyboard() {
        search_text.clearFocus()
        val `in` = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(search_text.windowToken, 0)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        initLoads()
        this.doubleBackToExitPressedOnce = true
        toast.showShort("Please click again to exit")
        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    fun CreateButton(){
        scroller.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY + 35 && extendedFab.isExtended) {     // Scroll Down
                extendedFab.shrink()
            }

            if(scrollY < oldScrollY - 35 && !extendedFab.isExtended) {      // Scroll Up
                extendedFab.extend()
            }

            if (scrollY == 0) {     // At the top
                extendedFab.extend()
            }
        })
    }
}