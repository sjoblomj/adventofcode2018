package org.sjoblomj.adventofcode.day4

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ShiftTest {
  @Test fun `Illegal input throws exceptions`() {
    assertFailsWith(IllegalAccessException::class) {
      parseIndata(listOf("First line is bogus", "[1518-04-06 00:09] falls asleep", "[1518-04-06 00:32] wakes up"))
    }
    assertFailsWith(IllegalAccessException::class) {
      parseIndata(listOf("[1518-04-06 00:00] Guard #499 begins shift", "Second line is bogus", "[1518-04-06 00:32] wakes up"))
    }
    assertFailsWith(IllegalAccessException::class) {
      parseIndata(listOf("[1518-04-06 00:00] Guard #499 begins shift", "[1518-04-06 00:09] falls asleep", "Third line is bogus"))
    }

    assertFailsWith(IllegalAccessException::class) {
      val illegalGuardId = listOf("[1518-04-06 00:00] Guard #4d99 begins shift", "[1518-04-06 00:09] falls asleep", "[1518-04-06 00:32] wakes up")
      parseIndata(illegalGuardId)
    }
    assertFailsWith(IllegalAccessException::class) {
      val illegalAsleepTime = listOf("[1518-04-06 00:00] Guard #499 begins shift", "[1518-04-06 00:0u9] falls asleep", "[1518-04-06 00:32] wakes up")
      parseIndata(illegalAsleepTime)
    }
    assertFailsWith(IllegalAccessException::class) {
      val illegalAwakeningTime = listOf("[1518-04-06 00:00] Guard #499 begins shift", "[1518-04-06 00:09] falls asleep", "[1518-04-06 00:3u2] wakes up")
      parseIndata(illegalAwakeningTime)
    }
    assertFailsWith(IllegalAccessException::class) {
      val illegalAwakeningYear = listOf("[1518-04-06 00:00] Guard #499 begins shift", "[1518-04-06 00:09] falls asleep", "[15a18-04-06 00:32] wakes up")
      parseIndata(illegalAwakeningYear)
    }
  }

  @Test fun `Shifts with multiple falling asleep and awakening times that are erroneous throws exception`() {
    assertFailsWith(IllegalAccessException::class) {
      parseIndata(listOf(
        "[1518-04-06 00:00] Guard #499 begins shift",
        "[1518-04-06 00:09] falls asleep",
        "[1518-04-06 00:32] wakes up",
        "[1518-04-06 00:34] wakes up", // Can't wake up twice without falling asleep first
        "[1518-04-06 00:39] falls asleep"
      ))
    }
    assertFailsWith(IllegalAccessException::class) {
      parseIndata(listOf(
        "[1518-04-06 00:00] Guard #499 begins shift",
        "[1518-04-06 00:09] falls asleep",
        "[1518-04-06 00:32] falls asleep", // Can't fall asleep twice without waking up first
        "[1518-04-06 00:34] wakes up",
        "[1518-04-06 00:39] falls asleep",
        "[1518-04-06 00:45] wakes up"
      ))
    }
    assertFailsWith(IllegalAccessException::class) {
      parseIndata(listOf(
        "[1518-04-06 00:00] Guard #499 begins shift",
        "[1518-04-06 00:09] falls asleep",
        "[1518-04-06 00:34] wakes up",
        "[1518-04-06 00:39] falls asleep" // Can't falling asleep without waking up
      ))
    }
  }

  @Test fun `Can parse single unsorted shift`() {
    val lst = parseIndata(listOf(
      "[1518-04-06 00:00] Guard #499 begins shift",
      "[1518-04-06 00:32] wakes up", // Should handle time stamps not being in order
      "[1518-04-06 00:09] falls asleep"))

    assertEquals(499, lst[0].id)
    assertEquals(listOf(9), lst[0].minuteWhenFallingAsleep)
    assertEquals(listOf(32), lst[0].minuteWhenAwoken)
  }

  @Test fun `Can parse single shift with multiple falling asleep and awakening times`() {
    val lst = parseIndata(listOf(
      "[1518-04-06 00:00] Guard #499 begins shift",
      "[1518-04-06 00:09] falls asleep",
      "[1518-04-06 00:32] wakes up",
      "[1518-04-06 00:34] falls asleep",
      "[1518-04-06 00:39] wakes up"
    ))

    assertEquals(499, lst[0].id)
    assertEquals(listOf(9, 34), lst[0].minuteWhenFallingAsleep)
    assertEquals(listOf(32, 39), lst[0].minuteWhenAwoken)
  }
}
