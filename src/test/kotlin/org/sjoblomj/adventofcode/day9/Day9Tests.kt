package org.sjoblomj.adventofcode.day9

import org.junit.Test
import kotlin.test.assertEquals as assertEquals

class Day9Tests {

  @Test fun `Can add marble`() {
    val turn = Turn(0)
    (1 .. 5).forEach { turn.add(it, 0) }

    turn.add(71, 2)
    assertEquals("(71) 2 3 4 5 0 1", turn.createMarbleString())

    turn.add(68, 2)
    assertEquals("(68) 4 5 0 1 71 2 3", turn.createMarbleString())

    turn.add(75, 3)
    assertEquals("(75) 1 71 2 3 68 4 5 0", turn.createMarbleString())

    turn.add(63, -4)
    assertEquals("(63) 4 5 0 75 1 71 2 3 68", turn.createMarbleString())
  }

  @Test fun `Can remove marble`() {
    val turn = Turn(0)
    (1 .. 5).forEach { turn.add(it, 0) }

    val a = turn.remove(2)
    assertEquals(1, a)
    assertEquals("(2) 3 4 5 0", turn.createMarbleString())

    val b = turn.remove(2)
    assertEquals(4, b)
    assertEquals("(5) 0 2 3", turn.createMarbleString())

    val c = turn.remove(-2)
    assertEquals(2, c)
    assertEquals("(3) 5 0", turn.createMarbleString())

    val d = turn.remove(-2)
    assertEquals(5, d)
    assertEquals("(0) 3", turn.createMarbleString())

    val e = turn.remove(0)
    assertEquals(0, e)
    assertEquals("(3)", turn.createMarbleString())

    val f = turn.remove(0)
    assertEquals(3, f)
    assertEquals("(3)", turn.createMarbleString())
  }

  @Test fun `Can calculate higest score`() {
    assertEquals(32, calculateHighestScore(playGame(9, 25)))
    assertEquals(8317, calculateHighestScore(playGame(10, 1618)))
    assertEquals(146373, calculateHighestScore(playGame(13, 7999)))
    assertEquals(2764, calculateHighestScore(playGame(17, 1104)))
    assertEquals(54718, calculateHighestScore(playGame(21, 6111)))
    assertEquals(37305, calculateHighestScore(playGame(30, 5807)))
  }


  private fun playGame(numberOfPlayers: Int, numberOfMarbles: Int): List<Player> {
    val players = (1..numberOfPlayers).map { Player(it, 0) }

    playGame(players, numberOfMarbles)
    return players
  }
}
