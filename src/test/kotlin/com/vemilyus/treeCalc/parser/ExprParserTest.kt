package com.vemilyus.treeCalc.parser

import com.vemilyus.treeCalc.model.AddSubOp
import com.vemilyus.treeCalc.model.Expr
import com.vemilyus.treeCalc.model.MulDivOp
import com.vemilyus.treeCalc.model.ParserResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExprParserTest {
    private val exprParser = ExprParser()

    @Test
    fun `it should parse a simple term`() {
        val source = "1.3"

        val expr = exprParser.parse(source)

        assertEquals(
            ParserResult.Ok(Expr.Term(1.3)),
            expr
        )
    }

    @Test
    fun `it should parse an addition`() {
        val source = "1.3 + 2.7"

        val expr = exprParser.parse(source)

        assertEquals(
            ParserResult.Ok(
                Expr.AddSub(
                    Expr.Term(1.3),
                    Expr.Term(2.7),
                    AddSubOp.Add
                )
            ),
            expr
        )
    }

    @Test
    fun `it should parse a subtraction`() {
        val source = "1.3 - 2.7"

        val expr = exprParser.parse(source)

        assertEquals(
            ParserResult.Ok(
                Expr.AddSub(
                    Expr.Term(1.3),
                    Expr.Term(2.7),
                    AddSubOp.Sub
                )
            ),
            expr
        )
    }

    @Test
    fun `it should parse a multiplication`() {
        val source = "5 * 3"

        val expr = exprParser.parse(source)

        assertEquals(
            ParserResult.Ok(
                Expr.MulDiv(
                    Expr.Term(5.0),
                    Expr.Term(3.0),
                    MulDivOp.Mul
                )
            ),
            expr
        )
    }

    @Test
    fun `it should parse a division`() {
        val source = "5 / 3"

        val expr = exprParser.parse(source)

        assertEquals(
            ParserResult.Ok(
                Expr.MulDiv(
                    Expr.Term(5.0),
                    Expr.Term(3.0),
                    MulDivOp.Div
                )
            ),
            expr
        )
    }

    @Test
    fun `it should parse a complex expression`() {
        val source = "1 + 2 * 3 / 4"

        val expr = exprParser.parse(source)

        assertEquals(
            ParserResult.Ok(
                Expr.AddSub(
                    Expr.Term(1.0),
                    Expr.MulDiv(
                        Expr.Term(2.0),
                        Expr.MulDiv(
                            Expr.Term(3.0),
                            Expr.Term(4.0),
                            MulDivOp.Div
                        ),
                        MulDivOp.Mul
                    ),
                    AddSubOp.Add
                )
            ),
            expr
        )
    }

    @Test
    fun `it should parse parentheses correctly`() {
        val source = "(1 + 2) * 3 / 4"

        val expr = exprParser.parse(source)

        assertEquals(
            ParserResult.Ok(
                Expr.MulDiv(
                    Expr.AddSub(
                        Expr.Term(1.0),
                        Expr.Term(2.0),
                        AddSubOp.Add
                    ),
                    Expr.MulDiv(
                        Expr.Term(3.0),
                        Expr.Term(4.0),
                        MulDivOp.Div
                    ),
                    MulDivOp.Mul
                )
            ),
            expr
        )
    }
}
