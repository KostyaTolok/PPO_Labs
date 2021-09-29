package com.lab1.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InputViewModel :ViewModel() {

    val currentInput: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val currentIsFloat: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
}