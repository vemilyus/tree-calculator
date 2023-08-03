package com.vemilyus.treeCalc.utils

interface Result<T, E> {
    fun asErr(): E?

    fun unwrap(): T
}
