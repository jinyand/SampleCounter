# SampleCounter
ViewModel, LiveData, DateBinding을 활용한 카운터 예제

### 0. 세팅
* Data Binding
  * build.gradle (app)
    ```kotlin
    android {
      dataBinding {
        enabled = true
      }
    }
    ```
    
  * xml 파일
    ```xml
    <layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools">

        <data>
            <variable
                name="viewmodel"
                type="com.example.SampleCounter.MyNumberViewModel" />
        </data>

        ...
      
    </layout>
    ```
    
* ViewModel, LiveData
  * build.gradle (app)
    ```kotlin
    dependencies {
        def lifecycle_version = "2.2.0"

        // viewmodel
        implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"

        // livedata
        implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    }
    ```
    
### 1. xml 레이아웃
* 숫자를 입력할 수 있는 EditText와 plus, minus 기능을 하는 각 Button, 수가 출력될 TextView로 구성
* 뷰 모델 값을 대입하기 위해 @{} 사용
* onClick 핸들러에 등록하여 이벤트를 처리할 메서드를 추가해서 사용
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="viewmodel"
            type="com.example.SampleCounter.MyNumberViewModel" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

        <TextView
            android:id="@+id/number_textview"
            android:text="@{String.valueOf(viewmodel.currentValue)}"
            ... />

        <EditText
            android:id="@+id/userinput_edittext"
            android:text="@={viewmodel.inputText}"
            ... />

        <Button
            android:id="@+id/plus_btn"
            android:text="더하기"
            android:onClick="@{()->viewmodel.addValue()}"
            ... />

        <Button
            android:id="@+id/minus_btn"
            android:text="빼기"
            android:onClick="@{()->viewmodel.minusValue()}"
            ... />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

### 2. ViewModel
```kotlin
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
```

### 3. activity
```kotlin
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
```
