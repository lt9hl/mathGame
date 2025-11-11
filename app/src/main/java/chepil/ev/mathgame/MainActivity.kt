package chepil.ev.mathgame

import android.os.Bundle
import android.os.health.TimerStat
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.emptyLongSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.random.Random
import kotlin.math.round
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import  java.util.concurrent.TimeUnit
import android.os.CountDownTimer
import android.os.SystemClock
import java.time.Clock
import java.time.LocalTime
import java.util.TimeZone

class MainActivity : AppCompatActivity() {
    var firstNumber = 0
    var secondNumber = 0
    var operand = ""
    var randomAnswer = "0"
    var correctAnswer = 0
    var correctAnswerDouble = 0.0
    var arrayOperands: List<String> = listOf("+", "-", "*", "/")

    var allTimers = mutableListOf<Int>()
    var timeStart : LocalTime = LocalTime.now()
    var timeToNumberInSeconds = 0
    var countCorrectAnswers = 0
    var countWrongAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<TextView>(R.id.isCorrect).isEnabled = false
        findViewById<TextView>(R.id.isWrong).isEnabled = false


    }

    fun isTrue(view: View) {
        if(checkAnswer())
            countCorrectAnswers++
        else
            countWrongAnswers++

        getPersentCorrectAnswers()
        updateStats()
        updateExample()
    }
    fun isWrong(view: View) {
        getPersentCorrectAnswers()
        updateStats()
        updateExample()
    }
    fun updateStats(){
        findViewById<TextView>(R.id.allExamples).text = (countWrongAnswers + countCorrectAnswers).toString()
        findViewById<TextView>(R.id.allCorrect).text = countCorrectAnswers.toString()
        findViewById<TextView>(R.id.allWrong).text = countWrongAnswers.toString()

        var timeEnd = LocalTime.now()

        timeToNumberInSeconds = 0

        if (timeEnd.minute > timeStart.minute)
            timeToNumberInSeconds = (timeEnd.minute - timeStart.minute) * 60

        timeToNumberInSeconds += timeEnd.second - timeStart.second

        allTimers.add(timeToNumberInSeconds)
        allTimers.sorted()

        if (allTimers.min() == timeToNumberInSeconds )
            findViewById<TextView>(R.id.minSeconds).text = timeToNumberInSeconds.toString()
        else if (allTimers.max() == timeToNumberInSeconds)
            findViewById<TextView>(R.id.maxSeconds).text = timeToNumberInSeconds.toString()
        findViewById<TextView>(R.id.averageSeconds).text = "%.2f".format(allTimers.average())

    }
    fun getPersentCorrectAnswers(){
        var persent = "%.2f".format(countCorrectAnswers.toDouble() / ( countCorrectAnswers.toDouble() + countWrongAnswers.toDouble() ) * 100.0 )
        findViewById<TextView>(R.id.persentCorrectAnswers).text = "$persent%"
    }

    fun startGame(view: View) {
        findViewById<TextView>(R.id.isCorrect).isEnabled = true
        findViewById<TextView>(R.id.isWrong).isEnabled = true

        resetStats()
        updateExample()
    }
    fun resetStats(){
        findViewById<TextView>(R.id.allExamples).text = "0"
        findViewById<TextView>(R.id.allCorrect).text = "0"
        findViewById<TextView>(R.id.allWrong).text = "0"
        findViewById<TextView>(R.id.minSeconds).text = "0"
        findViewById<TextView>(R.id.maxSeconds).text = "0"
        findViewById<TextView>(R.id.averageSeconds).text = "0.00"

        allTimers.clear()
        timeToNumberInSeconds = 0
        countCorrectAnswers = 0
        countWrongAnswers = 0
    }


    fun checkAnswer() : Boolean{
        getCorrectAnswer()
        if (correctAnswer.toString() == randomAnswer|| correctAnswerDouble == randomAnswer.toDouble())
            return true
        else
            return false

    }
    fun getCorrectAnswer() {
        when(operand){

            "+" -> correctAnswer = firstNumber + secondNumber
            "-" -> correctAnswer = firstNumber - secondNumber
            "*" -> correctAnswer = firstNumber * secondNumber
            "/" -> {
                if (firstNumber.toDouble() / secondNumber.toDouble() > firstNumber / secondNumber)
                    correctAnswerDouble = Math.round(firstNumber.toDouble() / secondNumber.toDouble() * 1000) / 100.0
                else
                    correctAnswer = firstNumber / secondNumber

            }
        }
    }

    fun updateExample(){
        timeStart = LocalTime.now()

        correctAnswerDouble = 0.0

        operand = arrayOperands.random()
        firstNumber = Random.nextInt(10,99)
        secondNumber = Random.nextInt(10,99)

        if (Random.nextBoolean()){
            getCorrectAnswer()
            if (correctAnswerDouble != 0.0)
                randomAnswer = correctAnswerDouble.toString()
            else
                randomAnswer = correctAnswer.toString()
        }
        else
            randomAnswer = Random.nextInt(10,99).toString()

        findViewById<TextView>(R.id.operation).text = operand
        findViewById<TextView>(R.id.firstNumber).text = firstNumber.toString()
        findViewById<TextView>(R.id.secondNumber).text = secondNumber.toString()
        findViewById<TextView>(R.id.result).text = randomAnswer


    }
}

