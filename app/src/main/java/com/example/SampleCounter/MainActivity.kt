package com.example.SampleCounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.SampleCounter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var myNumberViewModel: MyNumberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // ViewModelProvider 를 통해 뷰모델 가져오기
        // 라이프사이클을 가지고 있는 this 자기 자신을 넣어줌
        // 가져오고 싶은 뷰모델 클래스를 넣어서 뷰모델 가져오기
        myNumberViewModel = ViewModelProvider(this).get(MyNumberViewModel::class.java)

        binding.viewmodel = MyNumberViewModel()
        binding.lifecycleOwner = this

        // 뷰모델이 가지고 있는 값의 변경사항을 관찰할 수 있는 livedata를 옵저빙한다
        myNumberViewModel.currentValue.observe(this, Observer {
            Log.d("로그", "라이브데이터 값 변경 : $it")
            binding.numberTextview.text = it.toString()
        })

    }
}