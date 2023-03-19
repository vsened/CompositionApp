package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application): AndroidViewModel(application) {

    private val context = application

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level
    private var timer: CountDownTimer? = null
    private var countOfRightAnswers = 0
    private var countOfQuestions = 0


    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var _timerLD = MutableLiveData<String>()
    val timerLD: LiveData<String>
        get() = _timerLD


    private var _questionLD = MutableLiveData<Question>()
    val questionLD: LiveData<Question>
        get() = _questionLD


    private var _percentOfRightAnswersLD = MutableLiveData<Int>()
    val percentOfRightAnswersLD: LiveData<Int>
        get() = _percentOfRightAnswersLD

    private var _progressAnswersLD = MutableLiveData<String>()
    val progressAnswersLD: LiveData<String>
        get() = _progressAnswersLD

    private var _enoughPercentOfRightAnswersLD = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswersLD: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswersLD

    private var _enoughCountOfRightAnswersLD = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswersLD: LiveData<Boolean>
        get() = _enoughCountOfRightAnswersLD

    private var _minPercentOfRightAnswersLD = MutableLiveData<Int>()
    val minPercentOfRightAnswersLD: LiveData<Int>
        get() = _minPercentOfRightAnswersLD

    private var _gameResultLD = MutableLiveData<GameResult>()
    val gameResultLD: LiveData<GameResult>
        get() = _gameResultLD

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = getPercentOfRightQuestions()
        _percentOfRightAnswersLD.value = percent
        _progressAnswersLD.value = String.format(
            context.resources.getString(R.string.progress_answer),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoughCountOfRightAnswersLD.value =
            countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercentOfRightAnswersLD.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun chooseAnswer(answer: Int) {
        checkAnswer(answer)
        updateProgress()
        generateQuestion()
    }

    private fun checkAnswer(answer: Int) {
        val rightAnswer = questionLD.value?.rightAnswer
        if (rightAnswer == answer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        gameSettings = getGameSettingsUseCase(level)
        _minPercentOfRightAnswersLD.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECOND,
            MILLIS_IN_SECOND
        ) {
            override fun onTick(p0: Long) {
                _timerLD.value = formatTime(p0)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun generateQuestion() {
        _questionLD.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun finishGame() {
        _gameResultLD.value = GameResult(
            enoughCountOfRightAnswersLD.value == true
                    && enoughPercentOfRightAnswersLD.value == true,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    private fun getPercentOfRightQuestions(): Int {
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun formatTime(millis: Long): String {
        val minutes = millis / MILLIS_IN_SECOND / SECONDS_IN_MINUTE
        val seconds = millis / MILLIS_IN_SECOND % SECONDS_IN_MINUTE
        return String.format(TIMER_TEMPLATE, minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTE = 60
        private const val TIMER_TEMPLATE = "%02d:%02d"
    }
}