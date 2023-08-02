package com.vemilyus.treeCalc.model

import com.vemilyus.treeCalc.utils.Result

sealed interface Expr {
    data class MulDiv(val left: Expr, val right: Expr, val op: MulDivOp) : Expr

    data class AddSub(val left: Expr, val right: Expr, val op: AddSubOp) : Expr

    data class Term(val value: Double) : Expr
}

enum class MulDivOp {
    Mul,
    Div
}

enum class AddSubOp {
    Add,
    Sub
}

sealed interface ParserResult : Result<ParserResult.Ok, ParserResult.Err> {
    data class Ok(val expr: Expr) : ParserResult

    data class Err(
        val message: String,
        val position: IntRange?,
        val tokenizerErr: TokenizerResult.Err? = null,
        val cause: Throwable? = null
    ) : ParserResult

    override val isOk: Boolean
        get() = this is Ok

    override val isErr: Boolean
        get() = this is Err

    override fun unwrap(): Ok =
        this as? Ok ?: error("not Ok")
}
