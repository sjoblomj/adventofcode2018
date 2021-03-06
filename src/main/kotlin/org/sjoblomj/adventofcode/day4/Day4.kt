package org.sjoblomj.adventofcode.day4

import org.sjoblomj.adventofcode.readFile
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

private const val inputFile = "src/main/resources/inputs/day4.txt"


fun day4() {
  println("== DAY 4 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay4() }
  println("Finished Day 4 in $timeTaken ms\n")
}

private fun calculateAndPrintDay4() {
  val content = parseIndata(inputFile)
  println("Guard who is most asleep * minute guard that guard is the most asleep: ${calculatePart1Checksum(content)}")
  println("Guard who is most frequently asleep the same minute * that minute: ${calculatePart2Checksum(content)}")
}

internal fun parseIndata(fileName: String) = parseIndata(readFile(fileName))

internal fun calculatePart1Checksum(shifts: List<Shift>): Int {
  val guardId = getGuardIdWhoIsTheMostAsleep(shifts)
  val minute = getMinuteThatGuardIsTheMostAsleep(shifts, guardId)
  return guardId * minute
}


internal fun calculatePart2Checksum(shifts: List<Shift>): Int {
  val pair = shifts
    .map { it.id }
    .map { id -> Pair(id, getMinuteAndTheNumberOfTimesGuardIsAsleepTheMost(shifts, id)) }
    .maxBy { (_, pairOfMinuteAndCount) -> pairOfMinuteAndCount.second }
    ?: throw RuntimeException("Couldn't find the guard who is the most asleep")

  val guardId = pair.first
  val minute = pair.second.first

  return guardId * minute
}

internal fun getGuardIdWhoIsTheMostAsleep(shifts: List<Shift>): Int {
  return shifts
    .map { it.id }
    .map { id -> Pair(id, getTotalSleepTime(shifts, id)) }
    .maxBy { it.second }
    ?.first
    ?: throw RuntimeException("Couldn't find the guard who is the most asleep")
}

internal fun getMinuteThatGuardIsTheMostAsleep(shifts: List<Shift>, guardId: Int): Int {
  val pairOfMinuteAndCount = getMinuteAndTheNumberOfTimesGuardIsAsleepTheMost(shifts, guardId)
  return pairOfMinuteAndCount.first
}

private fun getMinuteAndTheNumberOfTimesGuardIsAsleepTheMost(shifts: List<Shift>, guardId: Int): Pair<Int, Int> {
  val shiftsForGuard = shifts.filter { it.id == guardId }

  return (0 until 60)
    .map { minute ->
      Pair(minute, shiftsForGuard
        .map { shift -> isAsleepAtGivenMinute(shift, minute) }
        .filter { isAsleep -> isAsleep }
        .count()
      )
    }
    .maxBy { it.second }
    ?: throw RuntimeException("Couldn't find the guard who is the most asleep")
}

private fun isAsleepAtGivenMinute(shift: Shift, minute: Int): Boolean {

  for (i in shift.minuteWhenFallingAsleep.indices) {
    if (minute >= shift.minuteWhenFallingAsleep[i] && minute < shift.minuteWhenAwoken[i])
      return true
  }
  return false
}

internal fun getTotalSleepTime(shifts: List<Shift>, guardId: Int): Int {
  return shifts
    .filter { it.id == guardId }
    .map { getTotalSleepTimeForShift(it) }
    .sum()
}

private fun getTotalSleepTimeForShift(shift: Shift): Int {
  return shift.minuteWhenFallingAsleep.zip(shift.minuteWhenAwoken) { timeAsleep, timeAwoken -> timeAwoken - timeAsleep }.sum()
}


internal fun visualize(indata: List<Shift>): String {
  val maxIdLength = indata.map { it.id.toString().length }.max()?:3
  val idSpaces = " ".repeat(maxIdLength)
  return "" +
    "Date   ID$idSpaces Minute\n" +
    "         $idSpaces 000000000011111111112222222222333333333344444444445555555555\n" +
    "         $idSpaces 012345678901234567890123456789012345678901234567890123456789\n" +
    indata.joinToString("\n") { visualizeShift(it, maxIdLength) }
}

private fun visualizeShift(shift: Shift, maxIdLength: Int): String {
  val date = shift.date.format(DateTimeFormatter.ofPattern("MM-dd"))
  val id = "#" + shift.id + " ".repeat(maxIdLength - shift.id.toString().length)

  var s = ""
  for (i in shift.minuteWhenFallingAsleep.indices) {
    s += ".".repeat(shift.minuteWhenFallingAsleep[i] - s.length)
    s += "#".repeat(shift.minuteWhenAwoken[i] - s.length)
  }
  s += ".".repeat(60 - s.length)

  return "$date  $id  $s"
}
