//package com.open.weatherapp
//
//import androidx.test.filters.SmallTest
//import com.open.weatherapp.database.WeatherRecord
//import com.open.weatherapp.repo.WeatherRepo
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import javax.inject.Inject
//import javax.inject.Named
//
//@HiltAndroidTest
//@SmallTest
//class WeatherDAOTest {
//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//    @Inject
//    @Named("waether")
//    lateinit var database: UserDatabase
//    private lateinit var userDao: UserDao
//
//    @Before
//    fun setup() {
//        hiltRule.inject()
//        userDao = database.userDao()
//    }
//
//    @After
//    fun tearDown() {
//        database.close()
//    }
//    @Test
//    fun insertUser() = runBlockingTest {
//        val user = WeatherRecord(
//            id = 1,
//            country = "IN",
//            city = "Borivali"
//        )
//        userDao.insertUser(user)
//        val allUsers = userDao.observeAllUsers().getOrAwaitValue()
//        assertThat(allUsers).contains(user)
//    }
//}