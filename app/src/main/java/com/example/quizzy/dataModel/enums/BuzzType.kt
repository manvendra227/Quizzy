package com.example.quizzy.dataModel.enums

//private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val QUIZ_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)
private val NEXT_BUZZ_PATTERN= longArrayOf(100,100,100)

enum class BuzzType(val pattern: LongArray) {
    QUIZ_OVER(QUIZ_OVER_BUZZ_PATTERN),
    COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
    NO_BUZZ(NO_BUZZ_PATTERN),
    NEXT(NEXT_BUZZ_PATTERN)
}