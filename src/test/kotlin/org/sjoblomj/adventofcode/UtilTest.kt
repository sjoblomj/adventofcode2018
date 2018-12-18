package org.sjoblomj.adventofcode

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import kotlin.test.*

class UtilTest {

  private val path = "out/test/test.txt"

  @Before fun setup() {
    deleteFileIfExists()
  }

  @After fun teardown() {
    deleteFileIfExists()
  }


  @Test fun `readFile throws exceptions for non-existing files`() {
    assertFailsWith(FileNotFoundException::class) {
      readFile("src/to/file/that/doesn't/exist.txt")
    }
  }

  @Test fun `readFile returns list of correct content`() {
    assertEquals(listOf("+1", "+1", "-2"), readFile("src/test/resources/inputs/test.txt"))
  }

  @Test fun `writeFile can write to file that does not already exists`() {
    assertFalse(File(path).exists())

    writeFile(path, "apa\nbepa")

    assertTrue(File(path).exists())
    assertEquals(listOf("apa", "bepa"), readFile(path))
  }

  @Test fun `writeFile overwrites files that already exist`() {
    File(path).writeText("This content will\nbe overwritten.")
    assertTrue(File(path).exists())

    writeFile(path, "apa\nbepa")

    assertTrue(File(path).exists())
    assertEquals(listOf("apa", "bepa"), readFile(path))
  }

  @Test fun `writeFile cannot write to Read Only files`() {
    File(path).writeText("This content can't\nbe overwritten.")
    File(path).setReadOnly()
    assertTrue(File(path).exists())

    assertFailsWith(FileNotFoundException::class) {
      writeFile(path, "apa\nbepa")
    }

    assertNotEquals(listOf("apa", "bepa"), readFile(path))
  }

  @Test fun `writeFile cannot write to files with illegal filenames`() {
    assertFailsWith(FileNotFoundException::class) {
      writeFile("path/that/can't/be/created§½!\"#¤%&/&()=?`´±\\}][{¥€$£@¡¶<>|¨^~¨´'*̣̣̣:·¸,", "apa\nbepa")
    }
  }


  @Test fun `printJson for empty input`() {
    assertEquals("\"\"", printJson(""))
  }

  @Test fun `printJson for int`() {
    assertEquals("71", printJson(71))
  }

  @Test fun `printJson for list`() {
    assertEquals("[71]", printJson(listOf(71)))
  }

  @Test fun `printJson for TestData class`() {
    val input = TestData("Apa Bepa", 30, listOf(71, 68, 63, 35))

    val result = printJson(input)

    assertEquals("{\"age\" : 30, \"favouriteNumbers\" : [71, 68, 63, 35], \"name\" : \"Apa Bepa\"}", result)
  }


  @Test fun `printJson for list of TestData class`() {
    val input = listOf(
      TestData("Apa Bepa", 30, listOf(71, 68, 63, 35)),
      TestData("Cepa Depa", 31, listOf(76, 78))
    )

    val result = printJson(input)

    val expectedResult = "[" +
      "{\"age\" : 30, \"favouriteNumbers\" : [71, 68, 63, 35], \"name\" : \"Apa Bepa\"}, " +
      "{\"age\" : 31, \"favouriteNumbers\" : [76, 78], \"name\" : \"Cepa Depa\"}" +
      "]"
    assertEquals(expectedResult, result)
  }


  data class TestData(val name: String, val age: Int, val favouriteNumbers: List<Int>)


  private fun deleteFileIfExists() {
    if (File(path).exists())
      File(path).delete()
  }
}
