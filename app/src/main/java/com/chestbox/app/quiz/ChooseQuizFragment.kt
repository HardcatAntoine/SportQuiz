package com.chestbox.app.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chestbox.app.R
import com.chestbox.app.databinding.FragmentChooseQuizBinding


class ChooseQuizFragment : Fragment() {
    lateinit var binding: FragmentChooseQuizBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseQuizBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bascketballBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseQuizFragment_to_basketballFragment)
        }
        binding.boxingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseQuizFragment_to_boxingFragment)
        }
        binding.hockeyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseQuizFragment_to_hockeyFragment)
        }
        binding.allBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chooseQuizFragment_to_allQuestionsFragment)
        }
    }
}