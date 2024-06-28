package com.toble.memo.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.toble.memo.databinding.ActivityEditBinding
import com.toble.memo.model.MemoData
import com.toble.memo.utils.KeyboardUtil


/**
 * 메모 수정, 입력 화면
 */
class EditActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "로그 - EditActivity:"
    }

    private lateinit var binding: ActivityEditBinding

//    private var memoState: String = "ADD"
//    private var beforeContent: String = ""
//    private var position: Int = -1

    private lateinit var memoData: MemoData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        setupKeyboardVisibilityListener() // 키보드 상태 리스너 등록

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("memoData", MemoData::class.java)
        } else {
            @Suppress("DEPRECATION") // 사용되지 않는 정보
            intent.getParcelableExtra("memoData") as? MemoData
        }?.let { memoData ->
            Log.d(TAG, "memoData: $memoData")
            this.memoData = memoData

            if (memoData.memoState == "ADD") {
                binding.actionTextView.text = "완료"
            } else {
                binding.actionTextView.text = "수정"
                binding.memoEditText.setText(memoData.content)
            }
        }

        binding.memoEditText.requestFocus()
    }

    override fun onResume() {
        super.onResume()

        // 뒤로가기 버튼 클릭
        binding.backImageView.setOnClickListener {
            processMemoAndFinish()
        }

        // 액션버튼(완료, 수정) 클릭
        binding.actionTextView.setOnClickListener {
            processMemoAndFinish()
        }

        // 키보드 숨기기
        binding.rootLayout.setOnClickListener {
            hideKeyboard()
            binding.memoEditText.clearFocus() // 포커스 제거
        }
    }

    /**
     * 액션버튼(완료, 수정) 클릭 시 처리
     */
    private fun processMemoAndFinish() {
        val memoText: String = binding.memoEditText.text.toString()

        if (memoText.isNotEmpty()) {
            setResultWithContent(memoText)
        } else {
            finish()
        }
    }


    /**
     * 결과값을 메인 화면에 전달
     */
    private fun setResultWithContent(content: String?) {
        if(memoData.memoState == "ADD" ){
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("content", content!!)
            }
            setResult(RESULT_OK, intent)

        } else {
            val intent = Intent(this, MainActivity::class.java)

            if(memoData.content == content){
                setResult(RESULT_CANCELED, intent)
            } else {
                intent.apply {
                    memoData.content = content!!
                    putExtra("memoData", memoData)
                }
                setResult(RESULT_OK, intent)
            }
        }
        finish()
    }

    /**
     * 키보드 숨기기
     */
    private fun hideKeyboard() {


        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }


    /**
     * 키보드 가시성 상태가 변경될 때 실행될 코드를 정의
     */
    private fun setupKeyboardVisibilityListener() {
        val parentView = (binding.root as ViewGroup).getChildAt(0) as ViewGroup

        KeyboardUtil.setKeyboardVisibilityListener(parentView, object : KeyboardUtil.OnKeyboardVisibilityListener {
            override fun onVisibilityChanged(isVisible: Boolean) {
                // 키보드 가시성 상태가 변경될 때 실행될 코드

                if (isVisible) {
                    //키보드가 올라왔을 때
                    Log.d(TAG, "키보드 올라옴")

                } else {
                    //키보드가 숨겨졌을 때
                    Log.d(TAG, "키보드 내려감")

                }
            }
        })
    }


    /**
     * onSupportNavigateUp() 메서드를 오버라이드하여
     * 뒤로가기 버튼을 눌렀을 때 동작을 정의
     */
    override fun onSupportNavigateUp(): Boolean {
        return true
    }
}
