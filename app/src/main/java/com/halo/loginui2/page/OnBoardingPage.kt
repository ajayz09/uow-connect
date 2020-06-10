package com.halo.loginui2.page

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.halo.loginui2.R

enum class OnBoardingPage(@StringRes val titleResource: Int,
                          @StringRes val descriptionResource: Int,
                          @DrawableRes val logoResource: Int) {

    UOW(R.string.android_title, R.string.android_description, R.drawable.uow_logo),
    IOS(R.string.ios_title, R.string.ios_description, R.drawable.events),
    SAFEZONE(R.string.safezone_title, R.string.safezone_description, R.drawable.safezone),
    CAREER(R.string.career_title, R.string.career_description, R.drawable.forum),
    COUNSELLING(R.string.counselling_title, R.string.counselling_description, R.drawable.counselling_logo)
}
