package com.toble.memo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toble.memo.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    //private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

       // initViewPager()
    }


}