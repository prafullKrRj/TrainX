package com.prafullkumar.trainxai

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val aiModule = module {
    viewModel { AIViewModel(get()) }
}