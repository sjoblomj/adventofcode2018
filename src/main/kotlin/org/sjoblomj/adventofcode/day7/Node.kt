package org.sjoblomj.adventofcode.day7

private val regex = "Step (?<dep>[A-Z]) must be finished before step (?<id>[A-Z]) can begin.".toRegex()


data class Node(val id: String, val prerequisites: MutableList<Node>, val dependers: MutableList<Node>) {

  fun setAsDependency(otherNode: Node) {
    dependers.add(otherNode)
    otherNode.prerequisites.add(this)
  }

  override fun toString(): String {
    return "$id - Prerequisites: ${prerequisites.map { it.id }}  -  Dependers: ${dependers.map { it.id }}"
  }
}


internal fun createNodesFromInput(input: List<String>): List<Node> {
  val pairs = input.map { parseLine(it) }
  val accumulatedDeps = groupDependencies(pairs)
  return createNodes(accumulatedDeps)
}

private fun groupDependencies(pairs: List<Pair<String, String>>): MutableMap<String, MutableList<String>> {
  val accumulatedDeps = mutableMapOf<String, MutableList<String>>()
  for ((id, dep) in pairs) {

    if (accumulatedDeps[id]?.isEmpty() != false) {
      accumulatedDeps[id] = mutableListOf(dep)
    } else {
      val list = accumulatedDeps[id]!! + listOf(dep)
      accumulatedDeps[id] = list.sorted().toMutableList()
    }
    if (accumulatedDeps[dep]?.isEmpty() != false) {
      accumulatedDeps[dep] = mutableListOf()
    }
  }
  return accumulatedDeps
}

private fun createNodes(accumulatedDeps: MutableMap<String, MutableList<String>>): List<Node> {

  val allNodes = accumulatedDeps.flatMap { it.value + it.key }.distinct().map { Node(it, mutableListOf(), mutableListOf()) }

  for (idAndDeps in accumulatedDeps) {
    val node = getNode(allNodes, idAndDeps.key)
    val deps = idAndDeps.value.map { depId -> getNode(allNodes, depId) }
    deps.forEach { dep -> dep.setAsDependency(node) }
  }
  return allNodes
}

private fun getNode(nodes: List<Node>, id: String): Node {
  return nodes.filter { it.id == id }[0]
}


internal fun parseLine(string: String): Pair<String, String> {

  val id = getRegexGroup(string, "id")
  val dep = getRegexGroup(string, "dep")
  return Pair(id, dep)
}

private fun getRegexGroup(input: String, group: String) =
  regex
    .matchEntire(input)
    ?.groups
    ?.get(group)
    ?.value
    ?: throw IllegalArgumentException("Can't find $group in input '$input'")
