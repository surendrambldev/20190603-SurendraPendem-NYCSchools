package com.jpmc.nycschools.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpmc.nycschools.R
import com.jpmc.nycschools.model.State
import com.jpmc.nycschools.utils.Utils
import com.jpmc.nycschools.viewmodels.HomeViewModel
import com.jpmc.nycschools.views.adapters.SchoolsRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    /**
     * ViewModel for HomeFragment
     */
    private val viewModel: HomeViewModel by viewModel()

    /**
     * RecyclerAdapter for SchoolsRecyclerView
     */
    private lateinit var schoolsRecyclerAdapter: SchoolsRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showShimmer.observe(viewLifecycleOwner, observerShowShimmer)
        //check internet connection and request data
        when (context?.run { Utils.isConnectedToNetwork(this) }) {
            true -> {
                setupRecyclerView()
                setState()
            }
            false -> no_internet?.visibility = View.VISIBLE
        }
    }

    /**
     * Observing pagination data state and updating it to adapter
     */
    private fun setState() {
        viewModel.getState().observe(this, Observer { state ->
            if (!viewModel.listIsEmpty()) {
                schoolsRecyclerAdapter.setState(state ?: State.COMPLETED)
            }
        })
    }

    /**
     * Setting up RecyclerView
     */
    private fun setupRecyclerView() {
        schoolsRecyclerAdapter = SchoolsRecyclerAdapter()
        schools_recycler_view?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        schools_recycler_view?.adapter = schoolsRecyclerAdapter
        viewModel.schoolsList.observe(viewLifecycleOwner, Observer {
            schoolsRecyclerAdapter.submitList(it)
        })
    }

    /**
     * Observer for showShimmer LiveData
     */
    private val observerShowShimmer = Observer<Boolean> { showShimmerLayout ->
        updateShimmerLayout(showShimmerLayout)
    }

    /**
     * Updates ShimmerLayout on UI
     *
     * @param showShimmerLayout when set to true shimmer layout will be visible
     */
    private fun updateShimmerLayout(showShimmerLayout: Boolean) {
        when (showShimmerLayout) {
            true -> {
                schools_recycler_view?.visibility = View.GONE
                shimmer_view?.visibility = View.VISIBLE
            }
            false -> {
                schools_recycler_view?.visibility = View.VISIBLE
                shimmer_view?.stopShimmer()
                shimmer_view?.visibility = View.GONE
            }
        }
    }
}