package com.github.gnolang.intellij.gno
/**
 * @license
 * Copyright Daniel Imms <http://www.growingwiththeweb.com>
 * Released under MIT license:
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Daniel Imms, http://www.growingwiththeweb.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Modified by Jackson Kearl <Microsoft/t-jakea@microsoft.com>
 */

/**
 * Represents a node in the binary tree, which has a key and a value, as well as left and right subtrees
 */

class Node<K, V>(
        var key: K,
        var value: V?
) {
    var left: Node<K, V>? = null
    var right: Node<K, V>? = null
    var height: Int = 0

    /**
     * Convenience function to get the height of the left child of the node,
     * returning -1 if the node is null.
     * @return The height of the left child, or -1 if it doesn't exist.
     */
    val leftHeight: Int
        get() = left?.height ?: -1

    /**
     * Convenience function to get the height of the right child of the node,
     * returning -1 if the node is null.
     * @return The height of the right child, or -1 if it doesn't exist.
     */
    val rightHeight: Int
        get() = right?.height ?: -1

    /**
     * Performs a right rotate on this node.
     * @return The root of the subtree; the node where this node used to be.
     */
    fun rotateRight(): Node<K, V> {
        val other = left ?: throw IllegalStateException("Left node is null")
        left = other.right
        other.right = this
        this.height = maxOf(leftHeight, rightHeight) + 1
        other.height = maxOf(other.leftHeight, this.height) + 1
        return other
    }

    /**
     * Performs a left rotate on this node.
     * @return The root of the subtree; the node where this node used to be.
     */
    fun rotateLeft(): Node<K, V> {
        val other = right ?: throw IllegalStateException("Right node is null")
        right = other.left
        other.left = this
        this.height = maxOf(leftHeight, rightHeight) + 1
        other.height = maxOf(other.rightHeight, this.height) + 1
        return other
    }
}

typealias DistanceFunction<K> = (K, K) -> Int
typealias CompareFunction<K> = (K, K) -> Int

/**
 * Represents how balanced a node's left and right children are.
 */
enum class BalanceState {
    /** Right child's height is 2+ greater than left child's height */
    UNBALANCED_RIGHT,
    /** Right child's height is 1 greater than left child's height */
    SLIGHTLY_UNBALANCED_RIGHT,
    /** Left and right children have the same height */
    BALANCED,
    /** Left child's height is 1 greater than right child's height */
    SLIGHTLY_UNBALANCED_LEFT,
    /** Left child's height is 2+ greater than right child's height */
    UNBALANCED_LEFT
}

class NearestNeighborDict<K : Comparable<K>, V>(
        start: Node<K, V>,
        private val distance: DistanceFunction<K>,
        private val compare: CompareFunction<K> = { a, b -> a.compareTo(b) }
) {

    private var root: Node<K, V>? = null

    init {
        insert(start.key, start.value)
    }

    /**
     * Gets the height of the tree.
     * @return The height of the tree.
     */
    fun height(): Int {
        return root?.height ?: 0
    }

    /**
     * Inserts a new node with a specific key into the tree.
     * @param key The key being inserted.
     * @param value The value being inserted.
     */
    private fun insert(key: K, value: V?) {
        root = insertNode(key, value, root)
    }

    /**
     * Inserts a new node with a specific key into the tree.
     * @param key The key being inserted.
     * @param root The root of the tree to insert in.
     * @return The new tree root.
     */
    private fun insertNode(key: K, value: V?, root: Node<K, V>?): Node<K, V> {
        var node = root ?: return Node(key, value)

        if (compare(key, node.key) < 0) {
            node.left = insertNode(key, value, node.left)
        } else if (compare(key, node.key) > 0) {
            node.right = insertNode(key, value, node.right)
        } else {
            return node
        }

        // Update height and rebalance tree
        node.height = maxOf(node.leftHeight, node.rightHeight) + 1
        val balanceState = getBalanceStateNode(node)

        if (balanceState == BalanceState.UNBALANCED_LEFT) {
            if (compare(key, node.left!!.key) < 0) {
                // Left case
                node = node.rotateRight()
            } else {
                // Left right case
                node.left = node.left?.rotateLeft()
                return node.rotateRight()
            }
        }

        if (balanceState == BalanceState.UNBALANCED_RIGHT) {
            if (compare(key, node.right!!.key) > 0) {
                // Right case
                node = node.rotateLeft()
            } else {
                // Right left case
                node.right = node.right?.rotateRight()
                return node.rotateLeft()
            }
        }

        return node
    }

    /**
     * Gets a node within the tree with a specific key, or the nearest neighbor to that node if it does not exist.
     * @param key The key being searched for.
     * @return The (key, value) pair of the node with key nearest the given key in value.
     */
    fun getNearest(key: K): Node<K, V> {
        return getNearestNode(key, root!!, root!!)
    }

    /**
     * Gets a node within the tree with a specific key, or the node closest (as measured by this._distance)
     * to that node if the key is not present.
     * @param key The key being searched for.
     * @param root The root of the tree to search in.
     * @param closest The current best estimate of the node closest to the node being searched for,
     * as measured by this._distance.
     * @return The (key, value) pair of the node with key nearest the given key in value.
     */
    private fun getNearestNode(key: K, root: Node<K, V>, closest: Node<K, V>): Node<K, V> {
        val result = compare(key, root.key)
        var bestClosest = closest

        if (result == 0) {
            return root
        }

        if (distance(key, root.key) < distance(key, closest.key)) {
            bestClosest = root
        }

        return if (result < 0) {
            root.left?.let { getNearestNode(key, it, bestClosest) } ?: bestClosest
        } else {
            root.right?.let { getNearestNode(key, it, bestClosest) } ?: bestClosest
        }
    }

    /**
     * Gets the balance state of a node, indicating whether the left or right
     * subtrees are unbalanced.
     * @param node The node to get the difference from.
     * @return The BalanceState of the node.
     */
    private fun getBalanceStateNode(node: Node<K, V>): BalanceState {
        val heightDifference = node.leftHeight - node.rightHeight
        return when (heightDifference) {
            -2 -> BalanceState.UNBALANCED_RIGHT
            -1 -> BalanceState.SLIGHTLY_UNBALANCED_RIGHT
            0 -> BalanceState.BALANCED
            1 -> BalanceState.SLIGHTLY_UNBALANCED_LEFT
            2 -> BalanceState.UNBALANCED_LEFT
            else -> throw IllegalStateException("AVL Tree should never be more than two levels unbalanced")
        }
    }
}
