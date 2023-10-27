package com.chestbox.app

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.lang.Exception
import java.util.Locale

class SplashFragment : Fragment() {

    private val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    lateinit var prefHelper: PrefHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefHelper = PrefHelper(requireContext().applicationContext)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    //проверка на эмулятор
    private fun isEmulator(): Boolean {
        if (BuildConfig.DEBUG) return false

        val phoneModel = Build.MODEL
        val buildProduct = Build.PRODUCT
        val buildHardware = Build.HARDWARE
        var result = (Build.FINGERPRINT.startsWith("generic")
                || phoneModel.contains("google_sdk")
                || phoneModel.lowercase(Locale.getDefault()).contains("droid4x")
                || phoneModel.contains("Emulator")
                || phoneModel.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || buildHardware == "goldfish"
                || Build.BRAND.contains("google")
                || buildHardware == "vbox86"
                || buildProduct == "sdk"
                || buildProduct == "google_sdk"
                || buildProduct == "sdk_x86"
                || buildProduct == "vbox86p"
                || Build.BOARD.lowercase(Locale.getDefault()).contains("nox")
                || Build.BOOTLOADER.lowercase(Locale.getDefault()).contains("nox")
                || buildHardware.lowercase(Locale.getDefault()).contains("nox")
                || buildProduct.lowercase(Locale.getDefault()).contains("nox"))
        if (result) return true
        result = result or (Build.BRAND.startsWith("generic") &&
                Build.DEVICE.startsWith("generic"))
        if (result) return true
        result = result or ("google_sdk" == buildProduct)
        return result
    }

    // проверка наличия интернета
    fun isInternetAvaliable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)
        return activeNetwork != null
    }


    fun init() {
        //проверям есть ли сохраненная ссылка на устройстве
        if (prefHelper.getSavedLink() == null) {
            //если ее нет - обращаемся к RemoteConfig
            //если устройство эмулятор или ссылка из RemoteConfig пустая строка -> открываем заглушку
           try {
               fetchLinkFromFirebase()
           } catch (e:Exception){
               Log.e("error", e.message ?: "")
               findNavController().navigate(R.id.action_splashFragment_to_secondSplashFragment)
           }
        } else {
            //ссылка есть
            //проверка подключения к интернету
            if (!isInternetAvaliable()) {
                //если интернет отключен переходим на экран с надписью (для работы приложения необходим доступ к сети)
                findNavController().navigate(R.id.action_splashFragment_to_internetDisableFragment)
            } else {
                //интернет доступен
                //переходим на webViewFragment
                val url = prefHelper.getSavedLink()!!
                val action: NavDirections =
                    SplashFragmentDirections.actionSplashFragmentToWebViewFragment(url)
                findNavController().navigate(action)
            }
        }
    }

    fun fetchLinkFromFirebase() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val url = firebaseRemoteConfig.getString("url")
                    if (url.isEmpty() || isEmulator()) {
                        findNavController().navigate(R.id.action_splashFragment_to_secondSplashFragment)
                    } else {
                        prefHelper.saveLink(url)
                        val action: NavDirections =
                            SplashFragmentDirections.actionSplashFragmentToWebViewFragment(url)
                        findNavController().navigate(action)
                    }
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_secondSplashFragment)
                }
            }
    }
}