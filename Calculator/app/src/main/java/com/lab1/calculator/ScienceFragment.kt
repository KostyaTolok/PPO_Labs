package com.lab1.calculator

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.lab1.calculator.databinding.FragmentScienceBinding
import net.objecthunter.exp4j.ExpressionBuilder

class ScienceFragment : Fragment(R.layout.fragment_science) {
    private val inputViewModel :InputViewModel by activityViewModels()
    lateinit var binding: FragmentScienceBinding
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
        binding = FragmentScienceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSin.setOnClickListener { onScience(it) }
        binding.buttonCos.setOnClickListener { onScience(it) }
        binding.buttonTan.setOnClickListener { onScience(it) }
        binding.buttonE.setOnClickListener { onScience(it) }
        binding.buttonLeftP.setOnClickListener { onScience(it) }
        binding.buttonRightP.setOnClickListener { onScience(it) }
        binding.buttonLg.setOnClickListener { onScience(it) }
        binding.buttonLn.setOnClickListener { onScience(it) }
        binding.buttonSqrt.setOnClickListener { onScience(it) }
        binding.buttonPower.setOnClickListener { onScience(it) }

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.buttonArcSin!!.setOnClickListener { onScience(it) }
            binding.buttonArcCos!!.setOnClickListener { onScience(it) }
            binding.buttonArcTan!!.setOnClickListener { onScience(it) }
            binding.buttonPi!!.setOnClickListener { onScience(it) }
            binding.buttonExp!!.setOnClickListener { onScience(it) }
        }
        else if(orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            binding.buttonOne!!.setOnClickListener { onDigit(it) }
            binding.buttonTwo!!.setOnClickListener { onDigit(it) }
            binding.buttonThree!!.setOnClickListener { onDigit(it) }
            binding.buttonFour!!.setOnClickListener { onDigit(it) }
            binding.buttonFive!!.setOnClickListener { onDigit(it) }
            binding.buttonSix!!.setOnClickListener { onDigit(it) }
            binding.buttonSeven!!.setOnClickListener { onDigit(it) }
            binding.buttonEight!!.setOnClickListener { onDigit(it) }
            binding.buttonNine!!.setOnClickListener { onDigit(it) }
            binding.buttonZero!!.setOnClickListener { onDigit(it) }

            binding.buttonAllClear!!.setOnClickListener { onClear(it) }
            binding.buttonEraseOne!!.setOnClickListener { onEraseOne(it) }
            binding.buttonDecimal!!.setOnClickListener { onDecimalPoint(it) }

            binding.buttonPlus!!.setOnClickListener { onOperator(it) }
            binding.buttonSubtract!!.setOnClickListener { onOperator(it) }
            binding.buttonDivide!!.setOnClickListener { onOperator(it) }
            binding.buttonMultiply!!.setOnClickListener { onOperator(it) }
            binding.buttonRemainder!!.setOnClickListener { onOperator(it) }
            binding.buttonEqual!!.setOnClickListener { onEqual(it) }
        }
    }

    private fun onScience(view: View) {
        val buttonText = (view as Button).text
        if (buttonText == "sin" || buttonText == "cos" || buttonText == "tan"
            || buttonText == "ln" || buttonText == "lg" || buttonText == "√"
            || buttonText.toString() == getString(R.string.arcSinText)
            || buttonText.toString() == getString(R.string.arcCosText)
            || buttonText.toString() == getString(R.string.arcTanText)
            || buttonText.toString() == getString(R.string.expText)
        ) {
            onTrigonometric(buttonText.toString())
        } else if (buttonText == "e" || buttonText.toString() == getString(R.string.piText)) {
            onConst(buttonText.toString())
        } else if (buttonText.toString() == getString(R.string.xPowerYText)) {
            onOperator(view)
        } else {
            onParenthesis(buttonText.toString())
        }

    }


    private fun onTrigonometric(input: String) {
        var toAdd = input
        val inputText = inputViewModel.currentInput.value
        when (toAdd) {
            "√" -> toAdd = "sqrt"
            "lg" -> toAdd = "log10"
            "ln" -> toAdd = "log"
            getString(R.string.expText) -> toAdd = "exp"
            getString(R.string.arcSinText) -> toAdd = "asin"
            getString(R.string.arcCosText) -> toAdd = "acos"
            getString(R.string.arcTanText) -> toAdd = "atan"
        }
        if (!inputText.isNullOrBlank() && (inputText.last().isDigit()||
                    inputText.last() == ')' ||
                    input.last() == getString(R.string.piText).first() ||
                    input.last() == 'e')) {
            inputViewModel.currentInput.value += "${getString(R.string.multiplyText)}$toAdd("
        }
        else if (inputText.isNullOrBlank() || inputText.last() != '.') {
            inputViewModel.currentInput.value +="$toAdd("
        }

    }

    private fun onParenthesis(toAdd: String) {
        val inputText = inputViewModel.currentInput.value
        if (inputText.isNullOrBlank() || inputText.last() != '.') {

            if (toAdd == "(" && !inputText.isNullOrBlank() &&
                (inputText.last() == ')' ||
                        inputText.last().isDigit() ||
                        inputText.last() == getString(R.string.piText).first() ||
                        inputText.last() == 'e')
            ) {
                inputViewModel.currentInput.value+="${getString(R.string.multiplyText)}$toAdd"
            } else {
                inputViewModel.currentInput.value+= toAdd
            }
        }
    }

    private fun onConst(toAdd: String) {
        val inputText = inputViewModel.currentInput.value
        if (!inputText.isNullOrBlank() && (inputText.last().isDigit() ||
                    inputText.last() == ')' ||
                    inputText.last() == getString(R.string.piText).first() ||
                    inputText.last() == 'e')
        ) {
            inputViewModel.currentInput.value += "${getString(R.string.multiplyText)}$toAdd"
        } else if (inputText.isNullOrBlank() || (!inputText.last().isDigit()
                    && inputText.last() != '.')) {
            inputViewModel.currentInput.value += toAdd
        }
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