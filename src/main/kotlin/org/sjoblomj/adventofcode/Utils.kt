package org.sjoblomj.adventofcode

import com.beust.klaxon.Klaxon
import java.io.File


fun readFile(path: String): List<String> = File(path).readLines()

fun writeFile(path: String, content: String) {
  File(path).writeText(content)
}

fun printJson(any: Any) = Klaxon().toJsonString(any)
