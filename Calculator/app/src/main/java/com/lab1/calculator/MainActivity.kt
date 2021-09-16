package com.lab1.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {

    var lastNumeric = false
    var lastPoint = false
    var lastOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onDigit(view: View) {
        val inputText = findViewById<TextView>(R.id.inputText)

        inputText.append((view as Button).text)
        lastNumeric = true
        lastOperator = false
        lastPoint = false
    }

    fun onClear(view: View) {
        val inputText = findViewById<TextView>(R.id.inputText)

        inputText.text = ""
        lastNumeric = false
        lastPoint = false
        lastOperator = false
    }

    fun onEraseOne(view: View) {
        val inputText = findViewById<TextView>(R.id.inputText)

        if (inputText.text.isNotBlank()) {
            inputText.text = inputText.text.dropLast(1)

            if (inputText.text.isNotBlank()) {
                val last = inputText.text.last()

                if (isOperator(last)) {
                    lastOperator = true
                    lastNumeric = false
                    lastPoint = false
                } else if (last == '.') {
                    lastOperator = false
                    lastNumeric = false
                    lastPoint = true
                } else {
                    lastOperator = false
                    lastNumeric = true
                    lastPoint = false
                }
            }
        }
    }

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !lastPoint && !lastOperator) {
            val inputText = findViewById<TextView>(R.id.inputText)

            inputText.append(".")
            lastNumeric = false
            lastOperator = false
        }
    }

    fun onOperator(view: View) {
        val inputText = findViewById<TextView>(R.id.inputText)
        if (lastNumeric && !lastOperator && !lastPoint) {
            inputText.append((view as Button).text)
            lastNumeric = false
            lastOperator = true
        }else if((view as Button).text == "-" && inputText.text.isBlank()){
            inputText.append((view as Button).text)
            lastNumeric = false
            lastOperator = true
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
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
            inputText.text = ""
        }
    }

    private fun isOperator(input: Char): Boolean {
        return input == '+' || input == '-'
                || input == '×' || input == '÷'
    }

}