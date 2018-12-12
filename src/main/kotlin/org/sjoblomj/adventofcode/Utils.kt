package org.sjoblomj.adventofcode

import java.io.File


fun readFile(path: String): List<String> {
  return try {
    File(path).readLines()
  } catch (e: Exception) {
    println(e)
    emptyList()
  }
}
