package org.sjoblomj.adventofcode.day2

import org.junit.Test
import java.util.Arrays.asList
import kotlin.test.*

class Day2Tests {
  @Test fun `Contains exactly n identical letters`() {
    assertFalse(containsExactlyTwoIdenticalLetters(""))
    assertFalse(containsExactlyThreeIdenticalLetters(""))

    assertFalse(containsExactlyTwoIdenticalLetters("abcdef"))
    assertFalse(containsExactlyThreeIdenticalLetters("abcdef"))

    assertTrue(containsExactlyTwoIdenticalLetters("bababc"))
    assertTrue(containsExactlyThreeIdenticalLetters("bababc"))

    assertTrue(containsExactlyTwoIdenticalLetters("abbcde"))
    assertFalse(containsExactlyThreeIdenticalLetters("abbcde"))

    assertFalse(containsExactlyTwoIdenticalLetters("abcccd"))
    assertTrue(containsExactlyThreeIdenticalLetters("abcccd"))

    assertTrue(containsExactlyTwoIdenticalLetters("aabcdd"))
    assertFalse(containsExactlyThreeIdenticalLetters("aabcdd"))

    assertTrue(containsExactlyTwoIdenticalLetters("abcdee"))
    assertFalse(containsExactlyThreeIdenticalLetters("abcdee"))

    assertFalse(containsExactlyTwoIdenticalLetters("ababab"))
    assertTrue(containsExactlyThreeIdenticalLetters("ababab"))
  }

  @Test fun `Can calculate checksum on empty input`() {
    assertEquals(0, calculateChecksum(emptyList()))
  }

  @Test fun `Can calculate checksum`() {
    val input = asList("abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab")
    assertEquals(4 * 3, calculateChecksum(input))
  }

  @Test fun `Exactly one char differs`() {
    assertFalse(exactlyOneCharDiffers("", ""))
    assertFalse(exactlyOneCharDiffers("abcde", "axcye"))
    assertFalse(exactlyOneCharDiffers("klmno", "pqrst"))
    assertTrue(exactlyOneCharDiffers("fghij", "fguij"))
  }

  @Test fun `Throws exception if input lengths differ when trying to find common part of strings`() {
    val input = listOf("abc", "defghi")
    assertFailsWith(IllegalArgumentException::class) {
      findCommonPartOfStringsWithOneCharDifferences(input)
    }
  }

  @Test fun `Can gracefully fail to find common part of strings that differ with exactly one char`() {
    val input = listOf("abcde", "dghij", "klmno", "pqrst", "fguij", "axcye", "wvxyz")
    assertNull(findCommonPartOfStringsWithOneCharDifferences(input))
  }

  @Test fun `Can find common part of strings that differ with exactly one char`() {
    val input0 = listOf("abcde", "fghij", "klmno", "pqrst", "fguij", "axcye", "wvxyz")
    assertEquals("fgij", findCommonPartOfStringsWithOneCharDifferences(input0))

    val input1 = listOf("abcde", "fghij", "klmno", "pqrst", "axcye", "wvxyz", "fguij")
    assertEquals("fgij", findCommonPartOfStringsWithOneCharDifferences(input1))
  }
}
