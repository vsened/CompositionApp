package com.example.composition.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameFinishedViewModel: ViewModel() {

    private var _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    fun countPercents(countOfAnswers: Int, countOfRightAnswers: Int) {
        _percentOfRightAnswers.value = ((countOfRightAnswers / countOfAnswers.toDouble()) * 100).toInt()
    }
}