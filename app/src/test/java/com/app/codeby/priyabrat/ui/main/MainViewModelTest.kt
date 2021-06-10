package com.app.codeby.priyabrat.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.codeby.priyabrat.data.Details
import com.app.codeby.priyabrat.data.Resource
import com.app.codeby.priyabrat.data.Weather
import com.app.codeby.priyabrat.data.WeatherResponse
import com.app.codeby.priyabrat.http.ApiServiceHelper
import com.app.codeby.priyabrat.utils.PrefUtils
import com.google.gson.Gson
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception


@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest : TestCase() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var preference: PrefUtils

    @Mock
    private lateinit var apiServiceHelper: ApiServiceHelper

    @Mock
    private lateinit var mainRepository: MainRepository

    private lateinit var mainViewModel: MainViewModel

    @Mock
    private lateinit var observable: Observer<Resource<WeatherResponse>>


    private var fakeLatitude = 12.9488884
    private var fakeLongitude = 77.7214587

    private var fakeWeatherFlowSuccess: Flow<WeatherResponse> = flow {
        emit(apiServiceHelper.getWeatherResponse(fakeLatitude, fakeLongitude))
    }

    @Before
    fun setup() {
        mainViewModel = MainViewModel(mainRepository)
    }

    @After
    fun tearsDown() {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetWeatherResponseSuccess() = runBlockingTest {
        mainViewModel.latitude = fakeLatitude
        mainViewModel.longitude = fakeLongitude

        val weather = WeatherResponse(
            "Bangalore",
            Details(100.5, 35, 50, 100.6, 102.0, 99.5),
            listOf(Weather("Heavy Rain fall with thunder storm","",1, "Thunderstorm")),
            System.currentTimeMillis()
        )
        doReturn(weather)
            .`when`(apiServiceHelper)
            .getWeatherResponse(fakeLatitude, fakeLongitude)

        doReturn(fakeWeatherFlowSuccess)
            .`when`(mainRepository)
            .getWeatherResponse(fakeLatitude, fakeLongitude)

        mainViewModel.getWeatherResponse()
        mainViewModel.liveDataWeatherResponse.observeForever(observable)

        verify(observable).onChanged(Resource.Success(weather))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetWeatherResponseFailWithZeroLocationData() = runBlockingTest {
        mainViewModel.latitude = 0.0
        mainViewModel.longitude = 0.0

        val weather = WeatherResponse(
            "Bangalore",
            Details(100.5, 35, 50, 100.6, 102.0, 99.5),
            listOf(Weather("Heavy Rain fall with thunder storm","",1, "Thunderstorm")),
            System.currentTimeMillis()
        )
        doReturn(weather)
            .`when`(apiServiceHelper)
            .getWeatherResponse(0.0, 0.0)

        doReturn(fakeWeatherFlowSuccess)
            .`when`(mainRepository)
            .getWeatherResponse(fakeLatitude, fakeLongitude)

        mainViewModel.getWeatherResponse()
        mainViewModel.liveDataWeatherResponse.observeForever(observable)

        verify(observable).onChanged(Resource.Error("Latitude & Longitude Can't be Zero"))
    }
}