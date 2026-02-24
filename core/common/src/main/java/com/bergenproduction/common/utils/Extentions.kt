package com.bergenproduction.common.utils

fun <T> MutableSet<T>.copyAndAdd(item: T): MutableSet<T> {
    return mutableSetOf<T>().apply {
        addAll(this@copyAndAdd)
        add(item)
    }
}

fun <T> MutableSet<T>.copyAndRemove(item: T): MutableSet<T> {
    return mutableSetOf<T>().apply {
        addAll(this@copyAndRemove)
        remove(item)
    }
}