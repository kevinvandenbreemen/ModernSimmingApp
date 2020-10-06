package com.vandenbreemen.util

/**
 * A simple publisher that allows objects to send data back to subscribers
 */
class SimplePublisher<T> {

    private val subscribers: MutableList<(T)->Unit> = mutableListOf()

    fun subscribe(subscriber: (T)->Unit) {
        subscribers.add(subscriber)
    }

    fun publish(value: T) {
        subscribers.forEach{ f->f(value) }
    }

}