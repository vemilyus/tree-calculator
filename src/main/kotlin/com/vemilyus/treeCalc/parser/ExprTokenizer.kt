package com.vemilyus.treeCalc.parser

import com.vemilyus.treeCalc.model.Token
import com.vemilyus.treeCalc.model.TokenType
import com.vemilyus.treeCalc.model.TokenizerResult

class ExprTokenizer {
    @Suppress("ComplexCondition", "CyclomaticComplexMethod", "ReturnCount")
    fun tokenize(source: String): TokenizerResult {
        val tokens = mutableListOf<Token>()

        var tokenType: TokenType? = null
        var start: Int? = 0
        var end: Int? = 0
        var doubleHasDot = false

        fun addToken() {
            val tt = tokenType ?: return
            val pos = (start ?: return)..<(end ?: return)

            tokenType = null
            start = null
            end = null
            doubleHasDot = false

            tokens.add(Token(tt, source.substring(pos), pos))
        }

        for ((position, char) in source.withIndex()) {
            when (char) {
                ' ', '\t' -> {
                    if (tokenType == TokenType.NUMBER && doubleHasDot && source[end!! - 1] == '.')
                        return unexpected('.', end!! - 1)

                    addToken()
                }

                '.' ->
                    if (
                        (tokenType == null || tokenType == TokenType.NUMBER || tokenType == TokenType.Sub) &&
                        !doubleHasDot
                    ) {
                        tokenType = TokenType.NUMBER
                        start = start ?: position
                        end = position + 1
                        doubleHasDot = true
                    } else
                        addToken()

                '-' ->
                    if (tokenType == null) {
                        tokenType = TokenType.Sub
                        start = position
                        end = position + 1
                    } else
                        addToken()

                in '0'..'9' ->
                    if (tokenType == null || tokenType == TokenType.NUMBER || tokenType == TokenType.Sub) {
                        tokenType = TokenType.NUMBER
                        start = start ?: position
                        end = position + 1
                    } else
                        addToken()

                '+', '*', '/' -> {
                    addToken()

                    tokenType =
                        when (char) {
                            '+' -> TokenType.Add
                            '*' -> TokenType.Mul
                            '/' -> TokenType.Div
                            else -> error("unreachable")
                        }

                    start = position
                    end = position + 1

                    addToken()
                }

                else -> return unexpected(char, position)
            }
        }

        // adds the final token if there is no whitespace at the end
        addToken()

        return TokenizerResult.Ok(tokens)
    }
}

private fun unexpected(char: Char, position: Int) =
    TokenizerResult.Err("Unexpected character '$char'", position)
