package com.lab1.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.lab1.calculator.databinding.FragmentBasicBinding
import net.objecthunter.exp4j.ExpressionBuilder

class BasicFragment : Fragment(R.layout.fragment_basic) {
    private val inputViewModel: InputViewModel by activityViewModels()
    lateinit var binding: FragmentBasicBinding
    private var isFloat = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inputViewModel.currentIsFloat.observe(this, Observer {
            isFloat = it
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasicBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEraseOne.setOnLongClickListener {
            onClear(it)
            return@setOnLongClickListener true
        }

        binding.buttonOne.setOnClickListener { onDigit(it) }
        binding.buttonTwo.setOnClickListener { onDigit(it) }
        binding.buttonThree.setOnClickListener { onDigit(it) }
        binding.buttonFour.setOnClickListener { onDigit(it) }
        binding.buttonFive.setOnClickListener { onDigit(it) }
        binding.buttonSix.setOnClickListener { onDigit(it) }
        binding.buttonSeven.setOnClickListener { onDigit(it) }
        binding.buttonEight.setOnClickListener { onDigit(it) }
        binding.buttonNine.setOnClickListener { onDigit(it) }
        binding.buttonZero.setOnClickListener { onDigit(it) }

        binding.buttonAllClear.setOnClickListener { onClear(it) }
        binding.buttonEraseOne.setOnClickListener { onEraseOne(it) }
        binding.buttonDecimal.setOnClickListener { onDecimalPoint(it) }

        binding.buttonPlus.setOnClickListener { onOperator(it) }
        binding.buttonSubtract.setOnClickListener { onOperator(it) }
        binding.buttonDivide.setOnClickListener { onOperator(it) }
        binding.buttonMultiply.setOnClickListener { onOperator(it) }
        binding.buttonRemainder.setOnClickListener { onOperator(it) }
        binding.buttonEqual.setOnClickListener { onEqual(it) }
    }

    private fun onDigit(view: View) {
        val inputText = inputViewModel.currentInput.value

        if (!inputText.isNullOrBlank() && (inputText.last() == ')'
                    || inputText.last() == '!'
                    || inputText.last() == 'e'
                    || inputText.last() == getString(R.string.piText).first())
        ) {
            inputViewModel.currentInput.value += getString(R.string.multiplyText)
        }
        inputViewModel.currentInput.value += (view as Button).text
    }

    private fun onClear(view: View) {

        inputViewModel.currentInput.value = ""
        inputViewModel.currentIsFloat.value = false
    }

    private fun onEraseOne(view: View) {
        val inputText = inputViewModel.currentInput.value

        if (!inputText.isNullOrBlank()) {
            val toDrop = inputText.last()
            inputViewModel.currentInput.value = inputText.dropLast(1)

            if (toDrop == '.') {
                inputViewModel.currentIsFloat.value = false
            } else if (isOperator(toDrop)) {
                inputViewModel.currentIsFloat.value = true
            }
        }
    }

    private fun isOperator(input: Char): Boolean {
        return input == '+' || input == '-' || input == '^'
                || input == getString(R.string.multiplyText).last()
                || input == getString(R.string.divideText).last()
    }

    private fun onDecimalPoint(view: View) {
        val inputText = inputViewModel.currentInput.value
        if (!inputText.isNullOrBlank() && inputText.last().isDigit()
            && !isFloat
        ) {
            inputViewModel.currentInput.value += "."
            inputViewModel.currentIsFloat.value = true
        }
    }

    private fun onOperator(view: View) {
        val inputText = inputViewModel.currentInput.value
        var toAdd = (view as Button).text
        if (toAdd.toString() == getString(R.string.xPowerYText)) {
            toAdd = "^"
        }
        if (!inputText.isNullOrBlank() && (inputText.last().isDigit()
                    || inputText.last() == ')'
                    || inputText.last() == '!'
                    || inputText.last() == 'e'
                    || inputText.last() == getString(R.string.piText).first())
        ) {
            inputViewModel.currentInput.value += toAdd
            inputViewModel.currentIsFloat.value = false
        } else if (toAdd == "-" && inputText.isNullOrBlank()) {
            inputViewModel.currentInput.value += toAdd
            inputViewModel.currentIsFloat.value = false
        }
    }

    private fun onEqual(view: View) {
        val inputText = inputViewModel.currentInput.value
        try {
            var input = inputText.toString().replace('÷', '/')
            input = input.replace('×', '*')


            val expression = ExpressionBuilder(input).build()
            val result = expression.evaluate()
            val longResult = result.toLong()

            if (result == longResult.toDouble()) {
                inputViewModel.currentInput.value = longResult.toString()
            } else {
                inputViewModel.currentInput.value = result.toString()
            }

            if (inputText != null) {
                if (inputText.contains('.')) {
                    inputViewModel.currentIsFloat.value = true
                }
            }
        } catch (e: Exception) {
            Toast.makeText(activity, "Ошибка", Toast.LENGTH_SHORT).show()
            inputViewModel.currentInput.value = ""
        }
    }


}