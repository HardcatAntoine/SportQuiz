package com.chestbox.app.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chestbox.app.R
import com.chestbox.app.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val args: ResultFragmentArgs by lazy {
        ResultFragmentArgs.fromBundle(requireArguments())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val score = args.mCorrectAnswers
        binding.tvScore.text = "Your Score is ${score}"
        binding.btnFinish.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_chooseQuizFragment)
        }
    }
}