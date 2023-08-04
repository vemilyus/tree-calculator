package com.vemilyus.treeCalc.parser

import com.vemilyus.treeCalc.model.Token
import com.vemilyus.treeCalc.model.TokenType
import com.vemilyus.treeCalc.model.TokenizerResult

class ExprLexer {
    @Suppress("ComplexCondition", "CyclomaticComplexMethod", "LongMethod", "NestedBlockDepth", "ReturnCount")
    fun tokenize(source: String): TokenizerResult {
        val tokens = mutableListOf<Token>()

        var tokenType: TokenType? = null
        var start: Int? = 0
        var end: Int? = 0
        var doubleHasDot = false

        fun addToken(): TokenizerResult.Err? {
            val tt = tokenType ?: return null
            val pos = (start ?: return null)..<(end ?: return null)

            if (tt == TokenType.NUMBER && doubleHasDot && source[pos.last] == '.')
                return unexpected('.', pos.last)

            tokenType = null
            start = null
            end = null
            doubleHasDot = false

            tokens.add(Token(tt, source.substring(pos), pos))

            return null
        }

        for ((position, char) in source.withIndex()) {
            when (char) {
                ' ', '\t' -> addToken()?.let { return it }

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
                        addToken()?.let { return it }

                '-' -> {
                    addToken()?.let { return it }

                    tokenType = TokenType.Sub
                    start = position
                    end = position + 1
                }

                in '0'..'9' ->
                    if (tokenType == null || tokenType == TokenType.NUMBER || tokenType == TokenType.Sub) {
                        tokenType = TokenType.NUMBER
                        start = start ?: position
                        end = position + 1
                    } else
                        addToken()?.let { return it }

                '+', '*', '/' -> {
                    addToken()?.let { return it }

                    tokenType =
                        when (char) {
                            '+' -> TokenType.Add
                            '*' -> TokenType.Mul
                            '/' -> TokenType.Div
                            else -> error("unreachable")
                        }

                    start = position
                    end = position + 1

                    addToken()?.let { return it }
                }

                '(' -> {
                    addToken()?.let { return it }

                    tokenType = TokenType.ParenLeft
                    start = position
                    end = position + 1

                    addToken()?.let { return it }
                }

                ')' -> {
                    addToken()?.let { return it }

                    tokenType = TokenType.ParenRight
                    start = position
                    end = position + 1

                    addToken()?.let { return it }
                }

                else -> return unexpected(char, position)
            }
        }

        // adds the final token if there is no whitespace at the end
        addToken()?.let { return it }

        return TokenizerResult.Ok(tokens)
    }
}

private fun unexpected(char: Char, position: Int) =
    TokenizerResult.Err("Unexpected character '$char'", position)
