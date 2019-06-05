package com.jpmc.nycschools.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.jpmc.nycschools.R
import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.model.Score
import com.jpmc.nycschools.utils.Utils
import com.jpmc.nycschools.viewmodels.SchoolDetailsViewModel
import kotlinx.android.synthetic.main.fragment_school_details.*
import org.koin.android.viewmodel.ext.android.viewModel


class SchoolDetailsFragment : Fragment() {

    /**
     * ViewModel for SchoolDetails
     */
    private val viewModel: SchoolDetailsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_school_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.showShimmer.observe(this, observerShowShimmer)
        viewModel.school.observe(this, observerSchool)
        viewModel.score.observe(this, observerScore)
        //check internet connection and request data
        when (context?.run { Utils.isConnectedToNetwork(this) }) {
            true -> {
                arguments?.let {
                    viewModel.fetchData(SchoolDetailsFragmentArgs.fromBundle(it).dbn)
                }
            }
            false -> {
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(details_shimmer_view).navigateUp()
            }
        }
    }

    /**
     * Observing school data LiveData
     */
    private val observerSchool = Observer<School?> { school ->
        updateSchoolDetailsOnUI(school)
    }

    /**
     * Observing school SAT scores LiveData
     */
    private val observerScore = Observer<Score?> { score ->
        updateScoresOnUI(score)
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
                details_shimmer_view?.visibility = View.VISIBLE
                scroll_view?.visibility = View.GONE
                header?.visibility = View.GONE
            }
            false -> {
                details_shimmer_view?.stopShimmer()
                details_shimmer_view?.visibility = View.GONE
                scroll_view?.visibility = View.VISIBLE
                header?.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Updates School details on UI
     */
    private fun updateSchoolDetailsOnUI(school: School?) {
        school?.run {
            name?.text = schoolName
            overview?.text = schoolOverview
            total_students?.text = totalStudents.toString()
            attendance_rate?.text =
                resources.getString(R.string.attendance_percentage, Math.round(attendanceRate * 100))
            activities?.text = extracurricularActivities
            website_details?.text = website
            phone_num?.text = phoneNumber
            location_details?.text = resources.getString(
                R.string.school_location_detailsformat,
                primaryAddress,
                city,
                stateCode,
                zip.toString()
            )
        }
    }

    /**
     * Updates School SAT scores on UI
     */
    private fun updateScoresOnUI(score: Score?) {
        val testTakers = score?.totalTestTakers?.toIntOrNull()
        val mathScore = score?.math?.toIntOrNull()
        val writingScore = score?.writing?.toIntOrNull()
        val readingScore = score?.reading?.toIntOrNull()
        val notAvailable = "N/A"
        total_test_takers.text =
            resources.getString(R.string.total_test_takers_format, testTakers?.toString() ?: notAvailable)
        math_score?.text = mathScore?.toString() ?: notAvailable
        writing_Score?.text = writingScore?.toString() ?: notAvailable
        reading_Score?.text = readingScore?.toString() ?: notAvailable
    }
}