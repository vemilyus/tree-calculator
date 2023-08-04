package com.vemilyus.treeCalc

import com.vemilyus.treeCalc.eval.ExprEvaluathor
import com.vemilyus.treeCalc.model.ParserResult
import com.vemilyus.treeCalc.parser.ExprParser
import kotlin.math.min

fun main() {
    println("Welcome to the Calculathor!")
    println("Feel free to enter a mathematical term using the basic")
    println("operators (+, -, *, /), simple floating points numbers")
    println("can also be used.")

    val exprParser = ExprParser()
    val exprEvaluathor = ExprEvaluathor()

    @Suppress("LoopWithTooManyJumpStatements")
    while (true) {
        print("> ")

        val source = readln()
        if (source.isBlank())
            continue

        val expr =
            when (val result = exprParser.parse(source)) {
                is ParserResult.Ok -> result.expr
                is ParserResult.Err -> {
                    printParserErr(source, result)
                    continue
                }
            }

        val result = exprEvaluathor.eval(expr)
        if (result.isInfinite()) {
            println()
            println("    ERROR: Division by zero is cringe!")
            println()
        } else
            println("= $result")
    }
}

private fun printParserErr(source: String, err: ParserResult.Err) {
    println()
    println("    ERROR: ${err.message}")
    println()

    val pos = err.position
    if (pos != null) {
        println("    $source")

        print("    ")
        print(" ".repeat(min(pos.first, source.length)))
        print("^")

        if (pos.last > pos.first) {
            print("~".repeat(pos.last - pos.first))
        }

        println()
        println()
    }
}
