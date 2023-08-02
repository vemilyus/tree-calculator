package com.vemilyus.treeCalc.utils

interface Result<T, E> {
    val isOk: Boolean
    val isErr: Boolean

    fun unwrap(): T
}
