package org.sjoblomj.adventofcode.day8

internal fun visualise(licenseFile: String, tree: Tree): String {
  val lengthOfOnePosition = 3
  val header = licenseFile.split(" ").joinToString("") { it.padStart(lengthOfOnePosition) }

  val lines = visualise(tree, 0, lengthOfOnePosition).sortedBy { it.level }
  val numberOfLevels = lines.map { it.level }.max() ?: 0

  val stringsWithLines = (0 .. numberOfLevels).map { level ->
    val relevantLines = lines
      .filter { it.level == level }
      .sortedBy { it.startPosition }

    relevantLines.mapIndexed { index, visualisation ->
      val prevEndPos = if (index > 0) relevantLines[index - 1].startPosition + (relevantLines[index - 1].line.length / lengthOfOnePosition) else 0
      val indendation = " ".repeat((visualisation.startPosition - prevEndPos) * lengthOfOnePosition)
      indendation + visualisation.line
    }.joinToString("")
  }

  return header + "\n" + stringsWithLines.joinToString("\n")
}

private fun visualise(tree: Tree, level: Int, lengthOfOnePosition: Int): List<Visualisation> {
  fun drawLine(length: Int) = " #-" + "-".repeat(lengthOfOnePosition * length)

  val currNodeLine = Visualisation(drawLine(tree.endedAt - tree.beganAt), tree.beganAt, level)
  return tree.children.flatMap { visualise(it, level + 1, lengthOfOnePosition) }.plus(currNodeLine)
}


private data class Visualisation(val line: String, val startPosition: Int, val level: Int)
