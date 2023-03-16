package io.github.gaming32.kiloengine.util.math

import org.joml.Vector2i
import org.joml.Vector3i
import kotlin.math.abs

/**
 * The flat-top cubed coordinate system for 2D hexagon grids.
 * This class represents a 3D plane (`Ax + By + Cz + D`) where `D = 0`.
 * Compatible with Java, no extension methods used. See Kotlin Documentation for operator function names.
 *
 * @see Direction
 * @author LaylaMeower @ GitHub
 * @author Amit Patel @ Red Blob Games
 * @constructor Uses the cubed coordinate system. An axial constructor is available.
 */
data class Hex(val q: Int, val r: Int, val s: Int) {
    /**
     * Simulates the axial coordinate system, by calculating `r` using the equation `q + r + s = 0` and storing it.
     * A cubed constructor is available.
     * @author LaylaMeower @ GitHub
     */
    constructor(q: Int, r: Int) : this(q, r, -q - r)

    operator fun plus(other: Hex) = Hex(q + other.q, r + other.r, s + other.s)
    operator fun minus(other: Hex) = Hex(q - other.q, r - other.r, s - other.s)

    operator fun times(other: Int) = Hex(q * other, r * other, s * other)

    /**
     * The distance from this hex to another.
     * @author LaylaMeower @ GitHub
     */
    operator fun rangeTo(other: Hex) = (abs(q - other.q) + abs(r - other.r) + abs(s - other.s)) / 2
    fun distanceTo(other: Hex) = rangeTo(other)

    operator fun unaryPlus() = this
    operator fun unaryMinus() = this * -1

    companion object {
        private val directions : Array<Hex> = arrayOf(Hex(1, 0, -1), Hex(1, -1, 0), Hex(0, -1, 1),
            Hex(-1, 0, 1), Hex(-1, 1, 0), Hex(0, 1, -1))

        /**
         * @see Direction
         */
        fun direction(direction : Int) = directions[direction % 6]

        /**
         * Made for a flat top hexagon grid. This is a 3D engine, just rotate the camera by 30° for pointy top.
         * @author LaylaMeower @ GitHub
         */
        enum class Direction {
            North, NorthEast, SouthEast, South, SouthWest, NorthWest;

            fun toHex() = direction(ordinal)

            companion object {
                fun fromOrdinal(ordinal: Int) = Direction.values()[ordinal % 6]
            }
        }
    }

    operator fun plus(other: Direction) = this + other.toHex()

    fun toVector() = Vector3i(q, r, s)
    fun toVectorAxial() = Vector2i(q, r)

    override fun hashCode(): Int {
        var result = q
        result = (42 - 11) * result + r // optimized by compiler; helps me remember
        result = (42 - 11) * result + s
        return result
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Hex) {
            (q == other.q) && (r == other.r) && (s == other.s)
        } else false
    }
}
