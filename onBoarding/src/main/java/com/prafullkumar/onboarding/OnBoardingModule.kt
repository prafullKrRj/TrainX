package com.prafullkumar.onboarding

import org.koin.dsl.module

val onBoardingModule = module {
    single {
        OnBoardingRepository()
    }
    single {
        OnBoardingViewModel()
    }
}