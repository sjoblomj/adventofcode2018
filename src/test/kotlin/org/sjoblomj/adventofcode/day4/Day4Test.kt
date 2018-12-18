package org.sjoblomj.adventofcode.day4

import org.junit.Test
import kotlin.test.assertEquals

class Day4Test {

  private val exampleIndata = listOf(
    "[1518-11-01 00:00] Guard #10 begins shift",
    "[1518-11-01 00:05] falls asleep",
    "[1518-11-01 00:25] wakes up",
    "[1518-11-01 00:30] falls asleep",
    "[1518-11-01 00:55] wakes up",
    "[1518-11-01 23:58] Guard #99 begins shift",
    "[1518-11-02 00:40] falls asleep",
    "[1518-11-02 00:50] wakes up",
    "[1518-11-03 00:05] Guard #10 begins shift",
    "[1518-11-03 00:24] falls asleep",
    "[1518-11-03 00:29] wakes up",
    "[1518-11-04 00:02] Guard #99 begins shift",
    "[1518-11-04 00:36] falls asleep",
    "[1518-11-04 00:46] wakes up",
    "[1518-11-05 00:03] Guard #99 begins shift",
    "[1518-11-05 00:45] falls asleep",
    "[1518-11-05 00:55] wakes up"
  )


  @Test fun `Can sort and parse unsorted indata`() {
    val lst = parseIndata("src/test/resources/inputs/unsorted_guardlist.txt")

    assertEquals(5, lst.size)

    assertEquals(499, lst[0].id)
    assertEquals(listOf(9, 43), lst[0].minuteWhenFallingAsleep)
    assertEquals(listOf(32, 55), lst[0].minuteWhenAwoken)

    assertEquals(1783, lst[1].id)
    assertEquals(listOf(8), lst[1].minuteWhenFallingAsleep)
    assertEquals(listOf(55), lst[1].minuteWhenAwoken)

    assertEquals(2657, lst[2].id)
    assertEquals(listOf(35), lst[2].minuteWhenFallingAsleep)
    assertEquals(listOf(47), lst[2].minuteWhenAwoken)

    assertEquals(751, lst[3].id)
    assertEquals(listOf(23), lst[3].minuteWhenFallingAsleep)
    assertEquals(listOf(50), lst[3].minuteWhenAwoken)

    assertEquals(619, lst[4].id)
    assertEquals(listOf(29), lst[4].minuteWhenFallingAsleep)
    assertEquals(listOf(59), lst[4].minuteWhenAwoken)
  }

  @Test fun `Can visualize a few days`() {
    val expectedVisualization = "" +
      "Date   ID   Minute\n" +
      "            000000000011111111112222222222333333333344444444445555555555\n" +
      "            012345678901234567890123456789012345678901234567890123456789\n" +
      "11-01  #10  .....####################.....#########################.....\n" +
      "11-02  #99  ........................................##########..........\n" +
      "11-03  #10  ........................#####...............................\n" +
      "11-04  #99  ....................................##########..............\n" +
      "11-05  #99  .............................................##########....."


    assertEquals(expectedVisualization, visualize(parseIndata(exampleIndata)))
  }

  @Test fun `Can count total sleep time for a given guard`() {
    assertEquals(20 + 25 + 5, getTotalSleepTime(parseIndata(exampleIndata), 10))
    assertEquals(10 + 10 + 10, getTotalSleepTime(parseIndata(exampleIndata), 99))
  }

  @Test fun `Can find guard who spends most time asleep`() {
    assertEquals(10, getGuardIdWhoIsTheMostAsleep(parseIndata(exampleIndata)))
  }

  @Test fun `Returns the first minute that a given guard is the most asleep if tie`() {
    val indata = listOf(
      "[1518-04-06 00:00] Guard #499 begins shift",
      "[1518-04-06 00:09] falls asleep",
      "[1518-04-06 00:32] wakes up",
      "[1518-04-07 00:00] Guard #499 begins shift",
      "[1518-04-07 00:11] falls asleep",
      "[1518-04-07 00:32] wakes up"
      )

      assertEquals(11, getMinuteThatGuardIsTheMostAsleep(parseIndata(indata), 499))
  }

  @Test fun `Can find the minute that a given guard is the most asleep`() {
    assertEquals(24, getMinuteThatGuardIsTheMostAsleep(parseIndata(exampleIndata), 10))
    assertEquals(45, getMinuteThatGuardIsTheMostAsleep(parseIndata(exampleIndata), 99))
  }

  @Test fun `Calculate Part 1 checksum`() {
    val guardId = 10
    val minuteMostOftenAsleep = 24

    assertEquals(guardId * minuteMostOftenAsleep, calculatePart1Checksum(parseIndata(exampleIndata)))
  }

  @Test fun `Calculate Part 2 checksum`() {
    val guardId = 99
    val minuteMostOftenAsleep = 45

    assertEquals(guardId * minuteMostOftenAsleep, calculatePart2Checksum(parseIndata(exampleIndata)))
  }
}
