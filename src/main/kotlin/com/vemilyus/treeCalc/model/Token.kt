package com.vemilyus.treeCalc.model

import com.vemilyus.treeCalc.utils.Result

data class Token(
    val type: TokenType,
    val content: String,
    val position: IntRange
) {
    val asDouble by lazy {
        content.toDouble()
    }
}

enum class TokenType {
    Add,
    Sub,
    Mul,
    Div,
    NUMBER
}

sealed interface TokenizerResult : Result<TokenizerResult.Ok, TokenizerResult.Err> {
    data class Ok(val tokens: List<Token>) : TokenizerResult

    data class Err(
        val message: String,
        val position: Int? = null,
        val cause: Throwable? = null
    ) : TokenizerResult

    override fun asErr() =
        this as? Err

    override fun unwrap(): Ok =
        this as? Ok ?: error("not Ok")
}
