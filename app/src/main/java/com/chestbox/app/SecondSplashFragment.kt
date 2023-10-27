package com.chestbox.app

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.chestbox.app.databinding.FragmentSecondSplashBinding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class SecondSplashFragment : Fragment() {
    lateinit var binding: FragmentSecondSplashBinding
    private val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.quizBtn.setOnClickListener {
            findNavController().navigate(R.id.action_secondSplashFragment_to_chooseQuizFragment)
        }
        binding.politicBtn.setOnClickListener {
            if (!isInternetAvaliable()){
                findNavController().navigate(R.id.action_secondSplashFragment_to_policyFailConnectionFragment)
            } else {
                fetchLinkFromFirebase()
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
                    val url = firebaseRemoteConfig.getString("p")
                    if (url.isEmpty()) {
                        Toast.makeText(requireContext(), "нет ссылки", Toast.LENGTH_LONG).show()
                        //findNavController().navigate(R.id.)
                    } else {
                        val action: NavDirections =
                            SecondSplashFragmentDirections.actionSecondSplashFragmentToWebViewFragment(
                                url
                            )
                        findNavController().navigate(action)
                    }
                }
            }
    }

    fun isInternetAvaliable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)
        return activeNetwork != null
    }
}