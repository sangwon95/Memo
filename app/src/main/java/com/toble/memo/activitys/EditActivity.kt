package com.toble.memo.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toble.memo.R
import com.toble.memo.databinding.ActivityEditBinding

/**
 * 메모 수정, 입력 화면
 */
class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        binding.memoEditText.setText("메모 수정, 입력 화면")
        binding.memoEditText.requestFocus();
    }

    override fun onResume() {
        super.onResume()

        binding.backImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("content", binding.memoEditText.text.toString())
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    // onSupportNavigateUp() 메서드를 오버라이드하여
    // 뒤로가기 버튼을 눌렀을 때 동작을 정의
    override fun onSupportNavigateUp(): Boolean {
        return true
    }
}