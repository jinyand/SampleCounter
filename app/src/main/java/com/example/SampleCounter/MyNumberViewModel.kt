package com.example.SampleCounter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyNumberViewModel : ViewModel() {

    // Mutable LiveData - 수정 가능
    // LiveData - 수정 불가능 (읽기 전용)

    // 내부에서 설정하는 자료형은 뮤터블로 변경가능하도록 설정
    private val _currentValue = MutableLiveData<Int>()

    // 변경되지 않는 데이터를 가져올 때 이름을 _ 언더스코어 없이 설정
    // 공개적으로 가져오는 변수는 private 이 아닌 퍼블릭으로 외부에서도 접근가능하도록 설정
    // 하지만 값을 직접 라이브데이터에 접근하지 않고 뷰모델을 통해 가져올 수 있도록 설정
    val currentValue: LiveData<Int>
        get() = _currentValue

    // 초기값 설정
    init {
        Log.d("로그", "MyNumberViewModel - 생성자 호출")
        _currentValue.value = 0
    }

    val inputText = MutableLiveData<String>()

    // 뷰모델이 가지고 있는 값을 변경
    fun addValue() {
        val plusCount: Int = inputText.value!!.toInt()
        _currentValue.value = _currentValue.value?.plus(plusCount)
    }

    fun minusValue() {
        val minusCount: Int = inputText.value!!.toInt()
        _currentValue.value = _currentValue.value?.minus(minusCount)
    }

}