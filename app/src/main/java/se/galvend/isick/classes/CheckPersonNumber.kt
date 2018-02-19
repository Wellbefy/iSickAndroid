package se.galvend.isick.classes

import android.util.Log

/**
 * Created by dennisgalven on 2018-02-19.
 * Class for checking if "person number" is correctly formatted
 */

class CheckPersonNumber {
    companion object {
        const val TAG = "CheckPersonNumber"
    }
    fun checkNumber(number: String): Boolean {
        if(number.count() != 11) return false

        val newNumber = number.replace("-", "")

        return lastDigit(newNumber)
    }

    private fun lastDigit(check: String): Boolean {
        if(check.isEmpty()) return false

        var numbers : List<Int> = emptyList()

        val lastNumberString = check.last()
        val restString = check.substring(0, check.count()-1)
        val lastNumber : Int

        try {
            lastNumber = lastNumberString.toString().toInt()
        }catch (e: NumberFormatException) {
            return false
        }
        Log.d(TAG, "lastNumber: $lastNumber")

        restString.forEach {
            try {
                numbers += it.toString().toInt()
            }catch (e: NumberFormatException) {
                return false
            }
        }

        Log.d(TAG, "number: $numbers")
        var newNumbers = emptyList<Int>()
        var even = false

        numbers.forEach {
            if(!even) {
                val twice = it * 2

                if(twice > 9) newNumbers += 1
                newNumbers += twice % 10

                even = true
            } else {
                newNumbers += it

                even = false
            }
        }

        Log.d(TAG, "newNumbers: $newNumbers")
        var calculation = 0

        newNumbers.forEach {
            calculation += it
            Log.d(TAG, "calculation: $calculation")
        }

        val lastDigit = (10 - (calculation % 10)) % 10
        Log.d(TAG, lastDigit.toString())

        return lastDigit == lastNumber
    }
}