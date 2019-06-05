package com.jpmc.nycschools.model

import com.google.gson.annotations.SerializedName

data class School(
    @SerializedName("dbn") val schoolId: String,
    @SerializedName("school_name") val schoolName: String,
    @SerializedName("overview_paragraph") val schoolOverview: String,
    @SerializedName("neighborhood") val neighborhood: String,
    @SerializedName("location") val location: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("fax_number") val faxNumber: String,
    @SerializedName("school_email") val schoolEmail: String,
    @SerializedName("website") val website: String,
    @SerializedName("grades2018") val gradesLastYear: String,
    @SerializedName("finalgrades") val finalGrades: String,
    @SerializedName("total_students") val totalStudents: Int,
    @SerializedName("extracurricular_activities") val extracurricularActivities: String,
    @SerializedName("attendance_rate") val attendanceRate: Double,
    @SerializedName("primary_address_line_1") val primaryAddress: String,
    @SerializedName("city") val city: String,
    @SerializedName("zip") val zip: Int,
    @SerializedName("state_code") val stateCode: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)