package org.sjoblomj.adventofcode.day9

import org.sjoblomj.adventofcode.readFile
import kotlin.system.measureTimeMillis

private const val inputFile = "src/main/resources/inputs/day9.txt"
private const val marbleThatAddsToScore = 23
private const val positionThatGetsAdded = -7
private const val positionToPlaceAt = 1


fun day9() {
  println("== DAY 9 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay9() }
  println("Finished Day 9 in $timeTaken ms\n")
}

private fun calculateAndPrintDay9() {
  val (numberOfPlayers, numberOfMarbles) = parseIndata(readFile(inputFile)[0])
  val players0 = (1 .. numberOfPlayers).map { Player(it, 0) }

  playGame(players0, numberOfMarbles)

  println("The highest score was ${calculateHighestScore(players0)}")

  val players1 = (1 .. numberOfPlayers).map { Player(it, 0) }
  playGame(players1, numberOfMarbles * 100)
  println("Playing with a hundred times more marbles, the highest score is ${calculateHighestScore(players1)}")
}

private fun parseIndata(indata: String): Pair<Int, Int> {
  fun parse(group: String) = "(?<players>[0-9]*) players; last marble is worth (?<points>[0-9]*) points".toRegex()
    .matchEntire(indata)
    ?.groups
    ?.get(group)
    ?.value
    ?: throw IllegalArgumentException("Can't find $group in input '$indata'")

  return parse("players").toInt() to parse("points").toInt()
}

internal fun calculateHighestScore(players: List<Player>) = players.map { it.score }.max()

internal fun playGame(players: List<Player>, numberOfMarbles: Int): Turn {
  val turn = Turn(0)
  for (currentMarble in 1 .. numberOfMarbles) {

    val player = players[(currentMarble - 1) % players.size]
    performTurn(currentMarble, turn, player)
  }
  return turn
}


internal fun performTurn(currentMarble: Marble, turn: Turn, player: Player) {
  if (currentMarble % marbleThatAddsToScore == 0) {

    val removedMarble = turn.remove(positionThatGetsAdded)
    player.score += removedMarble + currentMarble

  } else
    turn.add(currentMarble, positionToPlaceAt)
}


internal data class Player(val id: Int, var score: Long)

typealias Marble = Int
