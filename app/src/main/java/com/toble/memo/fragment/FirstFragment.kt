package com.toble.memo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.toble.memo.R
import com.toble.memo.databinding.FragmentFristBinding

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFristBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFristBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorPrimaryDark = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        binding.addImageView.setColorFilter(colorPrimaryDark)
    }
}