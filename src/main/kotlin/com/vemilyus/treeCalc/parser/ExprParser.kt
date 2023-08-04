package com.vemilyus.treeCalc.parser

import com.vemilyus.treeCalc.model.*

class ExprParser {
    private val tokenizer = ExprLexer()

    fun parse(source: String): ParserResult {
        val tokens =
            when (val result = tokenizer.tokenize(source)) {
                is TokenizerResult.Ok -> result.tokens
                is TokenizerResult.Err -> return ParserResult.Err(
                    "Failed to tokenize input",
                    result.position?.let { it..it },
                    result
                )
            }

        return parseMulDivExpr(tokens).first
    }
}

private val mulDivTypes = listOf(TokenType.Mul, TokenType.Div)
private val addSubTypes = listOf(TokenType.Add, TokenType.Sub, TokenType.NUMBER)

@Suppress("ReturnCount")
private fun parseMulDivExpr(tokens: List<Token>): Pair<ParserResult, List<Token>> {
    val (result, remaining) = parseAddSubExpr(tokens)
    val leftExpr = result.asErr()?.let { return it to remaining } ?: result.unwrap().expr

    if (remaining.isNotEmpty() && remaining.first().type in mulDivTypes) {
        val tokensMut = remaining.toMutableList()
        val (op, correctToken) = tokensMut.takeToken(TokenType.Mul, TokenType.Div)
        if (!correctToken)
            return ParserResult.Err(
                "Unexpected token '${op.content}', expected '+', '-', '*', or '/'",
                op.position
            ) to remaining

        val (rightRes, rightRem) = parseMulDivExpr(tokensMut)
        val rightExpr = rightRes.asErr()?.let { return it to rightRem } ?: rightRes.unwrap().expr

        return ParserResult.Ok(
            Expr.MulDiv(
                leftExpr,
                rightExpr,
                when (op.type) {
                    TokenType.Mul -> MulDivOp.Mul
                    TokenType.Div -> MulDivOp.Div
                    else -> error("unreachable")
                }
            )
        ) to rightRem
    }

    return ParserResult.Ok(leftExpr) to remaining
}

@Suppress("ReturnCount")
private fun parseAddSubExpr(tokens: List<Token>): Pair<ParserResult, List<Token>> {
    val (result, remaining) = parseParenExpr(tokens)
    val leftExpr = result.asErr()?.let { return it to remaining } ?: result.unwrap().expr

    if (remaining.isNotEmpty() && remaining.first().type in addSubTypes) {
        val tokensMut = remaining.toMutableList()
        val (op, correctToken) = tokensMut.takeToken(TokenType.Add, TokenType.Sub)
        if (op.type == TokenType.NUMBER && op.content.startsWith("-")) {
            return ParserResult.Err(
                "Unexpected number, did you mean '- ${op.content.trimStart('-')}'?",
                op.position
            ) to remaining
        } else if (!correctToken)
            return ParserResult.Err(
                "Unexpected token '${op.content}', expected '+', '-', '*', or '/'",
                op.position
            ) to remaining


        val (rightRes, rightRem) = parseMulDivExpr(tokensMut)
        val rightExpr = rightRes.asErr()?.let { return it to rightRem } ?: rightRes.unwrap().expr

        return ParserResult.Ok(
            Expr.AddSub(
                leftExpr,
                rightExpr,
                when (op.type) {
                    TokenType.Add -> AddSubOp.Add
                    TokenType.Sub -> AddSubOp.Sub
                    else -> error("unreachable")
                }
            )
        ) to rightRem
    }

    return ParserResult.Ok(leftExpr) to remaining
}

@Suppress("ReturnCount")
private fun parseParenExpr(tokens: List<Token>): Pair<ParserResult, List<Token>> {
    if (tokens.isEmpty())
        return eofErr() to tokens

    val tokensMut = tokens.toMutableList()
    val (_, correctToken) = tokensMut.takeToken(TokenType.ParenLeft)
    if (!correctToken)
        return parseTerm(tokens)

    val (result, remaining) = parseMulDivExpr(tokensMut)
    val expr = result.asErr()?.let { return it to remaining } ?: result.unwrap().expr

    if (remaining.isEmpty())
        return eofErr() to tokens

    val remMut = remaining.toMutableList()
    val (rightParen, rightCorrect) = remMut.takeToken(TokenType.ParenRight)
    if (!rightCorrect)
        return ParserResult.Err(
            "Unexpected token '${rightParen.content}', expected closing parenthesis",
            rightParen.position
        ) to remMut

    return ParserResult.Ok(expr) to remMut
}

@Suppress("ReturnCount")
private fun parseTerm(tokens: List<Token>): Pair<ParserResult, List<Token>> {
    if (tokens.isEmpty())
        return eofErr() to tokens

    val tokensMut = tokens.toMutableList()
    val (term, correctToken) = tokensMut.takeToken(TokenType.NUMBER)
    if (!correctToken)
        return ParserResult.Err("Unexpected token '${term.content}', expected a number", term.position) to tokensMut

    return ParserResult.Ok(Expr.Term(term.asDouble)) to tokensMut
}

private fun MutableList<Token>.takeToken(vararg tokenTypes: TokenType): Pair<Token, Boolean> {
    require(tokenTypes.isNotEmpty()) { "No token types specified" }

    val token = removeFirst()

    return token to (token.type in tokenTypes)
}

private fun eofErr() = ParserResult.Err("Unexpected end of input", Int.MAX_VALUE..Int.MAX_VALUE)
