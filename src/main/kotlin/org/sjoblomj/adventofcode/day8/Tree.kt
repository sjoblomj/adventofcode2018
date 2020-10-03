package org.sjoblomj.adventofcode.day8

data class Tree(val children: List<Tree>, val metadata: List<Int>, val beganAt: Int, val endedAt: Int)
