package org.sjoblomj.adventofcode.day8

import org.sjoblomj.adventofcode.readFile
import kotlin.system.measureTimeMillis

private const val inputFile = "src/main/resources/inputs/day8.txt"


fun day8() {
  println("== DAY 8 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay8() }
  println("Finished Day 8 in $timeTaken ms\n")
}

private fun calculateAndPrintDay8() {
  val indata = readFile(inputFile)[0]
  val tree = parseLicense(indata)
  println("Sum of all metadata entries: ${calculateMetadataSumPart1(tree)}")
  println("Value of root node: ${calculateMetadataForTree(tree)}")
}

internal fun calculateMetadataSumPart1(tree: Tree): Int = tree.metadata.sum() + tree.children.map { calculateMetadataSumPart1(it) }.sum()

internal fun calculateMetadataForTree(tree: Tree): Int {
  return if (tree.children.isEmpty())
    tree.metadata.sum()
  else
    tree.metadata.map { calculateChildMetadata(tree, it - 1) }.sum()
}

internal fun calculateChildMetadata(tree: Tree, index: Int) = if (index > tree.children.lastIndex) 0 else calculateMetadataForTree(tree.children[index])


internal fun parseLicense(licenseFile: String): Tree {
  val license = licenseFile.split(" ").map { it.toInt() }
  return createTree(license, 0)
}


private fun createTree(data: List<Int>, startPos: Int): Tree {
  val numberOfChildren = data[startPos]
  val numberOfMetadataEntries = data[startPos + 1]
  var index = 2 + startPos
  val children = mutableListOf<Tree>()

  for (i in 0 until numberOfChildren) {
    val child = createTree(data, index)
    children.add(child)
    index = child.endedAt + 1
  }

  return Tree(children, data.drop(index).take(numberOfMetadataEntries), startPos, index + numberOfMetadataEntries - 1)
}
