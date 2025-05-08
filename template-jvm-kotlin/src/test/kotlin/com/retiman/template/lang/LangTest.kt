package com.retiman.template.lang

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class LangTest : StringSpec({
  "kotlin has elvis operator for safe calls" {
    val text: String? = null
    val length = text?.length ?: 0

    length shouldBe 0
  }

  "kotlin can smart cast to a type" {
    val text: Any = "hello"
    if (text is String) {
      text shouldBe "hello"
    }
  }
})
