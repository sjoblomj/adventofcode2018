package org.sjoblomj.adventofcode.day9

internal class Turn(initialMarble: Marble) {
  private var marbles: CircleDeque<Marble>

  init {
    marbles = CircleDeque(initialMarble)
  }


  fun add(marble: Marble, position: Int) {
    marbles = marbles.rotate(position)
    marbles.push(marble)
    marbles = marbles.rotate(1)
  }

  fun remove(position: Int): Marble {
    marbles = marbles.rotate(position)
    val element = marbles.pop()
    marbles = marbles.rotate(1)

    return element
  }

  internal fun createMarbleString() = marbles.toList()
    .toList()
    .joinToString(" ") { if (it == marbles.value) "(${it})" else it.toString() }
}
