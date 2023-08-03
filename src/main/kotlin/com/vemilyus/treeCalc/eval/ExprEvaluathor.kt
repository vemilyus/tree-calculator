package com.vemilyus.treeCalc.eval

import com.vemilyus.treeCalc.model.AddSubOp
import com.vemilyus.treeCalc.model.Expr
import com.vemilyus.treeCalc.model.MulDivOp

class ExprEvaluathor {
    fun eval(expr: Expr): Double =
        when (expr) {
            is Expr.AddSub -> {
                val left = eval(expr.left)
                val right = eval(expr.right)

                when (expr.op) {
                    AddSubOp.Add -> left + right
                    AddSubOp.Sub -> left - right
                }
            }

            is Expr.MulDiv -> {
                val left = eval(expr.left)
                val right = eval(expr.right)

                when (expr.op) {
                    MulDivOp.Mul -> left * right
                    MulDivOp.Div -> left / right
                }
            }

            is Expr.Term -> expr.value
        }
}
