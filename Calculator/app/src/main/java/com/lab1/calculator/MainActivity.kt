package com.lab1.calculator

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {

    var isFloat = false
    val basicFragment = BasicFragment()
    val scienceFragment = ScienceFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, basicFragment)
                commit()
            }
        }
    }

    fun onDigit(view: View) {
        val inputText = findViewById<TextView>(R.id.inputText)

        if (inputText.text.isNotBlank() && (inputText.text.last() == ')'
                    || inputText.text.last() == '!'
                    || inputText.text.last() == 'e'
                    || inputText.text.last() == getString(R.string.piText).first())
        ) {
            inputText.append(getString(R.string.multiplyText))
        }
        inputText.append((view as Button).text)
    }

    fun onClear(view: View) {
        val inputText = findViewById<TextView>(R.id.inputText)

        inputText.text = ""
        isFloat = false
    }

    fun onEraseOne(view: View) {
        val inputText = findViewById<TextView>(R.id.inputText)

        if (inputText.text.isNotBlank()) {
            val toDrop = inputText.text.last()
            inputText.text = inputText.text.dropLast(1)

            if (toDrop == '.') {
                isFloat = false
            }
        }
    }

    fun onDecimalPoint(view: View) {
        val inputText = findViewById<TextView>(R.id.inputText)
        if (inputText.text.isNotBlank() && inputText.text.last().isDigit()
            && !isFloat
        ) {
            inputText.append(".")
            isFloat = true
        }
    }

    fun onOperator(view: View) {
        val inputText = findViewById<TextView>(R.id.inputText)
        var toAdd = (view as Button).text
        if (toAdd.toString() == getString(R.string.xPowerYText)) {
            toAdd = "^"
        }
        if (inputText.text.isNotBlank() && (inputText.text.last().isDigit()
                    || inputText.text.last() == ')'
                    || inputText.text.last() == '!'
                    || inputText.text.last() == 'e'
                    || inputText.text.last() == getString(R.string.piText).first())
        ) {
            inputText.append(toAdd)
            isFloat = false
        } else if (toAdd == "-" && inputText.text.isBlank()) {
            inputText.append(toAdd)
            isFloat = false
        }

    }

    fun onEqual(view: View) {
        val inputText = findViewById<TextView>(R.id.inputText)
        try {
            var input = inputText.text.toString().replace('÷', '/')
            input = input.replace('×', '*')


            val expression = ExpressionBuilder(input).build()
            val result = expression.evaluate()
            val longResult = result.toLong()

            if (result == longResult.toDouble()) {
                inputText.text = longResult.toString()
            } else {
                inputText.text = result.toString()
            }

            if (inputText.text.contains('.')){
                isFloat = true
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
            inputText.text = ""
        }
    }

    fun onSwitchToScience(view: View){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, scienceFragment)
            commit()
        }
    }

    fun onSwitchToBasic(view: View){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, basicFragment)
            commit()
        }
    }

    fun onScience(view: View) {
        val inputText = (view as Button).text
        if (inputText == "sin" || inputText == "cos" || inputText == "tan"
            || inputText == "ln" || inputText == "lg" || inputText == "√"
            || inputText.toString() == getString(R.string.arcSinText)
            || inputText.toString() == getString(R.string.arcCosText)
            || inputText.toString() == getString(R.string.arcTanText)
            || inputText.toString() == getString(R.string.expText)
        ) {
            onTrigonometric(inputText.toString())
        } else if (inputText == "e" || inputText.toString() == getString(R.string.piText)) {
            onConst(inputText.toString())
        } else if (inputText.toString() == getString(R.string.xPowerYText)) {
            onOperator(view)
        } else {
            onParenthesis(inputText.toString())
        }

    }


    private fun onTrigonometric(input: String) {
        var toAdd = input
        val inputText = findViewById<TextView>(R.id.inputText)
        when (toAdd) {
            "√" -> toAdd = "sqrt"
            "lg" -> toAdd = "log10"
            "ln" -> toAdd = "log"
            getString(R.string.expText) -> toAdd = "exp"
            getString(R.string.arcSinText) -> toAdd = "asin"
            getString(R.string.arcCosText) -> toAdd = "acos"
            getString(R.string.arcTanText) -> toAdd = "atan"
        }
        if (inputText.text.isNotBlank() && (inputText.text.last().isDigit()||
                    inputText.text.last() == ')' ||
                    input.last() == getString(R.string.piText).first() ||
                    input.last() == 'e')) {
            inputText.append("${getString(R.string.multiplyText)}$toAdd(")
        }
        else if (inputText.text.isBlank() || inputText.text.last() != '.') {
            inputText.append("$toAdd(")
        }

    }

    private fun onParenthesis(toAdd: String) {
        val inputText = findViewById<TextView>(R.id.inputText)
        val input = inputText.text
        if (input.isBlank() || input.last() != '.') {

            if (toAdd == "(" && input.isNotEmpty() &&
                (input.last() == ')' ||
                        input.last().isDigit() ||
                        input.last() == getString(R.string.piText).first() ||
                        input.last() == 'e')
            ) {
                inputText.append("${getString(R.string.multiplyText)}$toAdd")
            } else {
                inputText.append(toAdd)
            }
        }
    }

    private fun onConst(toAdd: String) {
        val inputText = findViewById<TextView>(R.id.inputText)
        val input = inputText.text
        if (input.isNotBlank() && (input.last().isDigit() ||
                    input.last() == ')' ||
                    input.last() == getString(R.string.piText).first() ||
                    input.last() == 'e')
        ) {
            inputText.append("${getString(R.string.multiplyText)}$toAdd")
        } else if (input.isBlank() || (!input.last().isDigit() && input.last() != '.')) {
            inputText.append(toAdd)
        }
    }

}