package com.jpmc.nycschools.views

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpmc.nycschools.R
import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.utils.Utils
import com.jpmc.nycschools.viewmodels.SearchViewModel
import com.jpmc.nycschools.views.adapters.RecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    /**
     * ViewModel for SearchFragment
     */
    private val viewModel: SearchViewModel by viewModel()

    /**
     * Search RecyclerAdapter
     */
    private lateinit var recyclerAdapter: RecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    /**
     * Setting up searchView by adding a textWatcher tolisten for changes in search query
     */
    private fun setupSearchView() {
        search_box?.addTextChangedListener(object : TextWatcher {
            val handler = Handler()
            override fun afterTextChanged(s: Editable?) {
                val textLength = s?.length ?: 0
                if (textLength > 0) clear_search.visibility = View.VISIBLE else View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    val query = s.toString().trim()
                    when (context?.run { Utils.isConnectedToNetwork(this) }) {
                        true -> if (query.length > 2) viewModel.search(query)
                        false -> Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
                    }
                }, 600)
            }
        })
        showOrHideKeyboard()
    }

    /**
     * Determines if to show keyboard or not
     */
    private fun showOrHideKeyboard() {
        search_box?.requestFocus()
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.showSoftInput(search_box, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Setting up Search Results RecyclerView
     */
    private fun setupRecyclerView() {
        search_rv?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerAdapter = RecyclerViewAdapter()
        search_rv?.adapter = recyclerAdapter
        viewModel.schools.observe(this, observerSchools)
    }


    /**
     * Observer for showShimmer LiveData
     */
    private val observerShowShimmer = Observer<Boolean> { isLoading ->
        updateShimmerLayout(isLoading)
    }

    /**
     * Updates ShimmerLayout on UI
     *
     * @param loading when set to true shimmer layout will be visible
     */
    private fun updateShimmerLayout(loading: Boolean) {
        when (loading) {
            true -> {
                searchProgressBar?.visibility = View.VISIBLE
                clear_search?.visibility = View.GONE
                search_rv.visibility = View.GONE
                shimmer_view_search.visibility = View.VISIBLE
            }
            false -> {
                searchProgressBar?.visibility = View.GONE
                clear_search?.visibility = View.VISIBLE
                shimmer_view_search.stopShimmer()
                shimmer_view_search.visibility = View.GONE
                search_rv.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Observing school data LiveData
     */
    private val observerSchools = Observer<List<School>> { schools ->
        recyclerAdapter.updateData(schools)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.isLoading.observe(viewLifecycleOwner, observerShowShimmer)
        viewModel.hasNoResults.observe(viewLifecycleOwner, Observer { noResults ->
            when (noResults) {
                true -> {
                    recyclerAdapter.updateData(emptyList())
                    search_no_results?.visibility = View.VISIBLE
                }
                false -> search_no_results?.visibility = View.GONE
            }
        })

        clear_search.setOnClickListener {
            search_box.text.clear()
            clear_search.visibility = View.GONE
            recyclerAdapter.updateData(emptyList())
            search_no_results.visibility = View.VISIBLE
        }

        search_rv?.setOnTouchListener { v, _ ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
            false
        }
        search_rv?.isNestedScrollingEnabled = false
        setupSearchView()
        setupRecyclerView()
    }
}