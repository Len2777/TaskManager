package com.example.jetpacktraning.domain.model

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class TimerViewModel : ViewModel() {
    private var isInitialized = false
    private var currentDuration: Long = 0L


    private var _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private var _timeLeft = MutableStateFlow(0L)
    val timeLeft: StateFlow<Long> = _timeLeft

    private var endTime = 0L
    private var timerJob: Job? = null

    fun startTimer(durationMillis: Long) {
        if (_isRunning.value) return

        endTime = SystemClock.elapsedRealtime() + durationMillis
        _isRunning.value = true

        timerJob = viewModelScope.launch {
            while (_isRunning.value && _timeLeft.value > 0) {
                val remaining = endTime - SystemClock.elapsedRealtime()
                _timeLeft.value = max(remaining, 0)
                delay(100)
            }
            _isRunning.value = false
        }
    }

    fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        _timeLeft.value = currentDuration
        pauseTimer()
    }

    fun initTimer(duration: Long) {
        if (!isInitialized) {
            currentDuration = duration
            _timeLeft.value = duration
            isInitialized = true
        }
    }
}

