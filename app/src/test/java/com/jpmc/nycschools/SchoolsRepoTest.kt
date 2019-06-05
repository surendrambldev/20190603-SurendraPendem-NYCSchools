package com.jpmc.nycschools

import com.jpmc.nycschools.repository.SchoolsRepo
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.standalone.inject

class SchoolsRepoTest : BaseTest() {

    private val schoolsRepo by inject<SchoolsRepo>()

    @Test
    fun searchSchoolsShouldReturnResult() {
        runBlocking {
            val formattedQuery = formatQuery("hil")
            val schools = schoolsRepo.fetchSearchResultsAsync(formattedQuery)
            assertEquals(8, schools?.size)
            assertEquals("Richmond Hill High School", schools?.first()?.schoolName)
            assertEquals("Richmond Hill", schools?.first()?.city)
            assertEquals("Richmond Hill", schools?.first()?.neighborhood)
            assertEquals("718-846-3335", schools?.first()?.phoneNumber)
            assertEquals("contact@richmondhillhs.org", schools?.first()?.schoolEmail)
            assertEquals("www.richmondhillhs.org", schools?.first()?.website)
            assertEquals(40.69517, schools?.first()?.latitude)
            assertEquals(-73.8338, schools?.first()?.longitude)
        }
    }

    @Test
    fun searchSchoolsShouldReturnNoResult() {
        runBlocking {
            val formattedQuery = formatQuery("hils")
            val schools = schoolsRepo.fetchSearchResultsAsync(formattedQuery)
            println("skp" + schools.toString())
            assertEquals(0, schools?.size)
        }
    }

    private fun formatQuery(query: String): String {
        return "school_name like '%${query.capitalize()}%'"
    }

    @Test
    fun searchSchoolsShouldReturnSchool() {
        runBlocking {
            val schools = schoolsRepo.fetchSchoolAsync("27Q475")
            assertEquals(1, schools?.size)
            assertEquals("Richmond Hill High School", schools?.first()?.schoolName)
            assertEquals("Richmond Hill", schools?.first()?.city)
            assertEquals("Richmond Hill", schools?.first()?.neighborhood)
            assertEquals("718-846-3335", schools?.first()?.phoneNumber)
            assertEquals("contact@richmondhillhs.org", schools?.first()?.schoolEmail)
            assertEquals("www.richmondhillhs.org", schools?.first()?.website)
            assertEquals(40.69517, schools?.first()?.latitude)
            assertEquals(-73.8338, schools?.first()?.longitude)
        }
    }

    @Test
    fun searchSchoolsShouldReturnSchoolScores() {
        runBlocking {
            val scores = schoolsRepo.fetchScoreAsync("27Q475")
            assertEquals("27Q475", scores?.first()?.schoolId)
            assertEquals(404, scores?.first()?.math?.toInt())
            assertEquals(382, scores?.first()?.reading?.toInt())
            assertEquals(368, scores?.first()?.writing?.toInt())
            assertEquals(404, scores?.first()?.totalTestTakers?.toInt())
        }
    }

    @Test
    fun searchSchoolsShouldReturnEmptyList() {
        runBlocking {
            val scores = schoolsRepo.fetchScoreAsync("27Q4755")
            assertEquals(0, scores?.size)
        }
    }
}