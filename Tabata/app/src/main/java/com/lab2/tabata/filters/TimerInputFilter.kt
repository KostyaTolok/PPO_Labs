package com.lab2.tabata.filters

import android.text.InputFilter
import android.text.Spanned
import java.lang.NumberFormatException


class TimerInputFilter : InputFilter {

    override fun filter(
        source: CharSequence, start: Int, end: Int,
        dest: Spanned?, dstart: Int, dend: Int
    ): CharSequence? {

        try {
            val input = (dest?.subSequence(0, dstart).toString() + source
                    + dest?.subSequence(dend, dest.length))

            if (Integer.parseInt(input) > 59 || Integer.parseInt(input) < 0) {
                return ""
            }
        } catch (nfe: NumberFormatException) {
            return "00"
        }

        return null
    }
}