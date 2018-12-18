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
  println("Resulting checksum for part 1 is ${calculatePart1Checksum(content)}")
  println("Resulting checksum for part 2 is ${calculatePart2Checksum(content)}")
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
  val res = getMinuteAndTheNumberOfTimesGuardIsAsleepTheMost(shifts, guardId)
  return res.first
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

  for (i in 0 until shift.minuteWhenFallingAsleep.size) {
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
  return "" +
    "Date   ID   Minute\n" +
    "            000000000011111111112222222222333333333344444444445555555555\n" +
    "            012345678901234567890123456789012345678901234567890123456789\n" +
    indata.joinToString("\n") { visualizeShift(it) }
}

private fun visualizeShift(shift: Shift): String {
  val date = shift.date.format(DateTimeFormatter.ofPattern("MM-dd"))

  var s = ""
  for (i in 0 until shift.minuteWhenFallingAsleep.size) {
    s += ".".repeat(shift.minuteWhenFallingAsleep[i] - s.length)
    s += "#".repeat(shift.minuteWhenAwoken[i] - s.length)
  }
  s += ".".repeat(60 - s.length)

  return "$date  #${shift.id}  $s"
}
