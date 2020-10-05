package org.sjoblomj.adventofcode.day9

import kotlin.math.abs

class CircleDeque<T> {
  val value: T
  private val node: Node<T>

  constructor(value: T) {
    this.value = value
    this.node = Node(value)
  }

  private constructor(node: Node<T>) {
    this.value = node.value
    this.node = node
  }

  fun rotate(position: Int): CircleDeque<T> {
    var nextNode = node

    if (position >= 0)
      for (i in 0 until position)
        nextNode = nextNode.next
    else
      for (i in 0 until abs(position))
        nextNode = nextNode.previous

    return CircleDeque(nextNode)
  }

  fun push(value: T) {
    node.push(value)
  }

  fun pop() = node.pop()


  fun toList(): List<T> {
    val nextNodes = generateSequence(node.next) { it.next }.takeWhile { it != node }.toList()
    val allNodes = listOf(node).plus(nextNodes)
    return allNodes.map { it.value }
  }
}

private data class Node<T>(val value: T) {
  var previous: Node<T> = this
  var next: Node<T> = this


  fun push(newValue: T) {
    val newNode = Node(newValue).also { it.previous = this }.also { it.next = next }
    next.previous = newNode
    next = newNode
  }

  fun pop(): T {
    if (previous == this || next == this)
      return value

    previous.next = next
    next.previous = previous
    return value
  }
}
