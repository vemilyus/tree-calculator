package com.vemilyus.treeCalc.parser

import com.vemilyus.treeCalc.model.Token
import com.vemilyus.treeCalc.model.TokenType
import com.vemilyus.treeCalc.model.TokenizerResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.exp

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExprTokenizerTest {
    private val exprTokenizer = ExprTokenizer()

    @Test
    fun `it should produce an empty token list`() {
        val source = "     "

        val tokens = exprTokenizer.tokenize(source)

        assertEquals(
            TokenizerResult.Ok(emptyList()),
            tokens
        )
    }

    @Test
    fun `it should tokenize a bunch of stuff`() {
        val source = "  + - * / 1.2 -23"

        val tokens = exprTokenizer.tokenize(source)

        assertEquals(
            TokenizerResult.Ok(
                listOf(
                    Token(
                        TokenType.Add,
                        "+",
                        2..<3
                    ),
                    Token(
                        TokenType.Sub,
                        "-",
                        4..<5
                    ),
                    Token(
                        TokenType.Mul,
                        "*",
                        6..<7
                    ),
                    Token(
                        TokenType.Div,
                        "/",
                        8..<9
                    ),
                    Token(
                        TokenType.NUMBER,
                        "1.2",
                        10..<13
                    ),
                    Token(
                        TokenType.NUMBER,
                        "-23",
                        14..<17
                    )
                )
            ),
            tokens
        )
    }

    @Test
    fun `it should fail to tokenize some inputs`() {
        var source = "asd"

        var result = exprTokenizer.tokenize(source)

        assertEquals(
            TokenizerResult.Err("Unexpected character 'a'", 0),
            result
        )

        source = "12. / 32"

        result = exprTokenizer.tokenize(source)

        assertEquals(
            TokenizerResult.Err("Unexpected character '.'", 2),
            result
        )

        source = "12 + . 32"

        result = exprTokenizer.tokenize(source)

        assertEquals(
            TokenizerResult.Err("Unexpected character '.'", 5),
            result
        )
    }
}
