package com.jpmc.nycschools.model

import com.google.gson.annotations.SerializedName

data class Score(
    @SerializedName("dbn") val schoolId: String,
    @SerializedName("num_of_sat_test_takers") val totalTestTakers: String,
    @SerializedName("sat_critical_reading_avg_score") val reading: String,
    @SerializedName("sat_math_avg_score") val math: String,
    @SerializedName("sat_writing_avg_score") val writing: String
)