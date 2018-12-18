package org.sjoblomj.adventofcode.day4

import java.time.LocalDate

private val idRegex         = ".* Guard #(?<num>[0-9]+) begins shift".toRegex()
private val fallAsleepRegex = "\\[.* 00:(?<num>[0-9]{2})] falls asleep".toRegex()
private val wakeUpRegex     = "\\[.* 00:(?<num>[0-9]{2})] wakes up".toRegex()
private val dateOfLastLine  = "\\[(?<num>[0-9-]+) [0-9]{2}:[0-9]{2}] .*".toRegex()

data class Shift(val id: Int, val minuteWhenFallingAsleep: List<Int>, val minuteWhenAwoken: List<Int>, val date: LocalDate)

internal fun parseIndata(indata: List<String>): List<Shift> {
  val data = indata.sorted()

  val shiftList = mutableListOf<Shift>()
  var i = 0
  while (i < data.size) {

    val id = getNumberFromLine(data, i++, idRegex)

    val sleepTimes = mutableListOf<Int>()
    val awakeTimes = mutableListOf<Int>()
    i = parseSleepAndAwakeTimes(data, i, sleepTimes, awakeTimes)

    val date = getDateFromLine(data, i - 1, dateOfLastLine)

    shiftList.add(Shift(id, sleepTimes, awakeTimes, date))
  }

  return shiftList
}

private fun parseSleepAndAwakeTimes(data: List<String>, index: Int, sleepTimes: MutableList<Int>, awakeTimes: MutableList<Int>): Int {
  var i = index
  while (i < data.size && !data[i].contains(idRegex)) {
    val sleepTime = getNumberFromLine(data, i++, fallAsleepRegex)
    val awakeTime = getNumberFromLine(data, i++, wakeUpRegex)

    assertLegalValues(sleepTime, sleepTimes, awakeTime, awakeTimes)

    sleepTimes.add(sleepTime)
    awakeTimes.add(awakeTime)
  }
  return i
}

private fun assertLegalValues(sleepTime: Int, sleepTimes: MutableList<Int>, awakeTime: Int, awakeTimes: MutableList<Int>) {
  if (sleepTimes.isNotEmpty()) {
    if (sleepTimes.last() >= awakeTime)
      throw IllegalAccessException("Time for falling asleep was after waking up")
    if (awakeTimes.last() >= sleepTime)
      throw IllegalAccessException("Time for awakening was after falling asleep")
  }
}

private fun getNumberFromLine(data: List<String>, index: Int, regex: Regex): Int {
  val res = getStringFromLine(data, index, regex)
  return res.toInt()
}

private fun getDateFromLine(data: List<String>, index: Int, regex: Regex): LocalDate {
  val res = getStringFromLine(data, index, regex)
  return LocalDate.parse(res)
}

private fun getStringFromLine(data: List<String>, index: Int, regex: Regex): String {
  if (index >= data.size)
    throw IllegalAccessException("Expected to be able to match '$regex', but found no more indata")
  if (!data[index].contains(regex))
    throw IllegalAccessException("Expected line to match '$regex', but was '${data[index]}'")

  return regex.matchEntire(data[index])?.groups?.get("num")?.value
    ?: throw IllegalAccessException("Unable to extract requested group")
}
