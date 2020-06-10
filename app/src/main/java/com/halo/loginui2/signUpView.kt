package com.halo.loginui2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.halo.loginui2.page.OnBoardingPage
import com.halo.loginui2.view.OnboardingPageView
import kotlinx.android.synthetic.main.activity_signup_view.*

class signUpView : AppCompatActivity() {

    private val adapter = OnboardingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_view)

        val data = OnBoardingPage
                .values()
                .map { onboardingPageData ->
                    val pageView = OnboardingPageView(this)
                    pageView.setPageData(onboardingPageData)

                    pageView
                }

        adapter.setData(data)
        onboardingView.setAdapter(adapter)
    }
}