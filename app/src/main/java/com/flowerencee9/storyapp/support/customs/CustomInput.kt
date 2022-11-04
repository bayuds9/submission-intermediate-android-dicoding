package com.flowerencee9.storyapp.support.customs

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.ViewInputTextBinding
import com.flowerencee9.storyapp.support.isEmailValid
import com.google.android.material.textfield.TextInputLayout

class CustomInput : ConstraintLayout {
    private lateinit var mContext: Context
    private lateinit var binding: ViewInputTextBinding

    private var listener: InputListener? = null

    enum class TYPE {TEXT, PASSWORD, EMAIL}
    private var inputType = TYPE.TEXT
    private var inputLength : Int? = null

    interface InputListener {
        fun afterTextChanged(input: String)
    }

    constructor(context: Context) : super(context, null) {
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(context)

    }

    constructor(context: Context, attributeSet: AttributeSet?, defstyleAttr: Int) : super(
        context,
        attributeSet,
        defstyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
        binding = ViewInputTextBinding.bind(
            LayoutInflater.from(mContext).inflate(R.layout.view_input_text, this, true)
        )
        binding.inputTextEdit.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            listener?.afterTextChanged(p0.toString())
            if (inputLength != null) {
                if (p0.toString().length < inputLength!!) {
                    setHelperText(String.format(context.getString(R.string.info_password_length), inputLength.toString()))
                } else {
                    setHelperText("")
                }
            }
        }

    }

    fun setMinLength(length: Int) {
        inputLength = length
    }

    fun setHint(hint: String) {
        binding.inputController.hint = hint
    }

    fun setHelperText(text: String, color: Int? = null) {
        with(binding) {
            inputController.helperText = text
            inputController.setHelperTextColor(
                ColorStateList.valueOf(
                    color ?: ContextCompat.getColor(context, R.color.purple_200)
                )
            )
        }
    }

    fun setInpuType(type: TYPE) {
        inputType = type
        binding.inputTextEdit.inputType = when(type) {
            TYPE.PASSWORD -> InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            TYPE.EMAIL -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            else -> InputType.TYPE_CLASS_TEXT
        }
    }

    fun setCleartext(states: Boolean){
        binding.inputController.endIconMode = if (states) TextInputLayout.END_ICON_CLEAR_TEXT else TextInputLayout.END_ICON_NONE
    }

    fun setLines(lines: Int, max: Int = 5) {
        binding.inputTextEdit.gravity = Gravity.TOP
        binding.inputTextEdit.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        binding.inputTextEdit.minLines = lines
        binding.inputTextEdit.maxLines = max

    }

    fun setVisiblePassword(){
        binding.inputController.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
    }

    fun setListener(inputListener: InputListener) {
        listener = inputListener
    }

    fun getText(): String {
        return binding.inputTextEdit.text.toString()
    }

    fun isValid(): Boolean{
        return if (inputType == TYPE.EMAIL) getText().isEmailValid() else getText().isNotEmpty()
    }
}