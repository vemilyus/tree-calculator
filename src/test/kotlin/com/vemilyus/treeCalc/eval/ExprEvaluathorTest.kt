package com.vemilyus.treeCalc.eval

import com.vemilyus.treeCalc.parser.ExprParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExprEvaluathorTest {
    private val exprEvaluathor = ExprEvaluathor()
    private val exprParser = ExprParser()

    @Test
    fun `it should evaluate a simple expression`() {
        val expr = exprParser.parse("1 + 2").unwrap().expr

        val result = exprEvaluathor.eval(expr)

        assertEquals(
            3.0,
            result
        )
    }

    @Test
    fun `it should evaluate a more complex expression`() {
        val expr = exprParser.parse("1 + 2 * 3 / 4").unwrap().expr

        val result = exprEvaluathor.eval(expr)

        assertEquals(
            2.5,
            result
        )
    }

    @Test
    fun `it should evaluate an expression containing parentheses`() {
        val expr = exprParser.parse("(1 + 2) * 3 / 4").unwrap().expr

        val result = exprEvaluathor.eval(expr)

        assertEquals(
            2.25,
            result
        )
    }
}
