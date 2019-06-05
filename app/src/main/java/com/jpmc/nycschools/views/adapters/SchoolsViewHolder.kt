package com.jpmc.nycschools.views.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.jpmc.nycschools.R
import com.jpmc.nycschools.model.School
import com.jpmc.nycschools.views.HomeFragmentDirections
import com.jpmc.nycschools.views.SearchFragmentDirections
import kotlinx.android.synthetic.main.recyler_item.view.*

/**
 * SchoolsViewHolder for Schools RecyclerView
 */
class SchoolsViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(school: School?, isFromSearch: Boolean) {
        with(itemView) {
            school?.run {
                school_name?.text = schoolName
                school_location?.text = resources.getString(R.string.school_location_format, neighborhood, city)
                content_top?.setOnClickListener {
                    val action = when (isFromSearch) {
                        true -> SearchFragmentDirections.actionSearchFragmentToSchoolDetailsFragment(
                            schoolId
                        )
                        false -> HomeFragmentDirections.actionHomeFragmentToSchoolDetailsFragment(
                            schoolId
                        )
                    }
                    Navigation.findNavController(it).navigate(action)
                }
                call_school?.setOnClickListener { openDialer(phoneNumber) }
                email_school?.setOnClickListener { openEmail(schoolEmail) }
                nav_to_school?.setOnClickListener { openMaps(longitude, latitude) }
                school_website?.setOnClickListener { openBrowser(website) }
            }
        }
    }

    /**
     * launches an intent to open URL in browser
     */
    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val formattedUrl = when {
            !url.startsWith("http://") -> {
                "http://$url"
            }
            else -> url
        }
        intent.data = Uri.parse(formattedUrl)
        itemView.context.startActivity(intent)
    }

    /**
     * launches an intent to open Google Maps Application and navigate
     */
    private fun openMaps(longitude: Double, latitude: Double) {
        val uri = Uri.parse("google.navigation:q=$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        with(itemView.context) {
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Google Maps app not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * launches an intent to open E-Mail client to send an E-Mail
     */
    private fun openEmail(schoolEmail: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        val intentChooserTitle = "Select email client:"
        intent.data = Uri.parse("mailto:$schoolEmail")
        itemView.context.startActivity(Intent.createChooser(intent, intentChooserTitle))
    }

    /**
     * launches an intent to open caller app to make a call to given phone number
     */
    private fun openDialer(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        itemView.context.startActivity(intent)
    }

    companion object {
        fun create(parent: ViewGroup): SchoolsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyler_item, parent, false)
            return SchoolsViewHolder(view)
        }
    }
}