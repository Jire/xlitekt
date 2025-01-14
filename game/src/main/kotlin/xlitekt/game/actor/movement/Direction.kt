package xlitekt.game.actor.movement

sealed class Direction {
    object North : Direction()
    object East : Direction()
    object South : Direction()
    object West : Direction()
    object NorthEast : Direction()
    object SouthEast : Direction()
    object SouthWest : Direction()
    object NorthWest : Direction()
    object NorthNorthEast : Direction()
    object NorthNorthWest : Direction()
    object WestNorthWest : Direction()
    object WestSouthWest : Direction()
    object EastNorthEast : Direction()
    object EastSouthEast : Direction()
    object SouthSouthEast : Direction()
    object SouthSouthWest : Direction()

    /**
     * Returns the corresponding opcode used for this direction for player movement.
     * @param useSixteenPoints Walking uses eight point cardinal direction and running uses sixteen point cardinal direction.
     *
     * Running direction opcodes.
     *  ______________________
     * | [11][12][13][14][15] |
     * | [09][##][##][##][10] |
     * | [07][##][XX][##][08] |
     * | [05][##][##][##][06] |
     * | [00][01][02][03][04] |
     *  ----------------------
     *
     * Walking direction opcodes.
     *  ______________
     * | [05][06][07] |
     * | [03][XX][04] |
     * | [00][01][02] |
     *  --------------
     */
    fun playerOpcode(useSixteenPoints: Boolean = false) = when (this) {
        is NorthEast -> if (useSixteenPoints) 15 else 7
        is NorthNorthEast -> 14
        is North -> if (useSixteenPoints) 13 else 6
        is NorthNorthWest -> 12
        is NorthWest -> if (useSixteenPoints) 11 else 5
        is EastNorthEast -> 10
        is WestNorthWest -> 9
        is East -> if (useSixteenPoints) 8 else 4
        is West -> if (useSixteenPoints) 7 else 3
        is EastSouthEast -> 6
        is WestSouthWest -> 5
        is SouthEast -> if (useSixteenPoints) 4 else 2
        is SouthSouthEast -> 3
        is South -> if (useSixteenPoints) 2 else 1
        is SouthSouthWest -> 1
        is SouthWest -> 0
    }

    /**
     * Returns the corresponding opcode used for this direction for npc movement.
     *  ______________
     * | [00][01][02] |
     * | [03][XX][04] |
     * | [05][06][07] |
     *  --------------
     */
    fun npcOpcode() = when (this) {
        // TODO Running support.
        is SouthEast -> 7
        is South -> 6
        is SouthWest -> 5
        is East -> 4
        is West -> 3
        is NorthEast -> 2
        is North -> 1
        is NorthWest -> 0
        else -> throw IllegalStateException("Direction opcode not found for npc. Direction was $this")
    }

    fun angle() = when (this) {
        is NorthWest -> 768
        is NorthNorthWest -> 896
        is North -> 1024
        is NorthNorthEast -> 1152
        is NorthEast -> 1280
        is WestNorthWest -> 640
        is EastNorthEast -> 1408
        is West -> 512
        is East -> 1536
        is WestSouthWest -> 384
        is EastSouthEast -> 1664
        is SouthWest -> 256
        is SouthSouthWest -> 128
        is South -> 2048
        is SouthSouthEast -> 1920
        is SouthEast -> 1792
    }

    fun fourPointCardinalDirection() = angle() % 256 == 0

    override fun toString(): String = javaClass.simpleName

    companion object {
        fun directionFromDelta(deltaX: Int, deltaZ: Int): Direction = when {
            // 16 point direction.
            deltaX == 2 && deltaZ == 2 -> NorthEast // 15
            deltaX == 1 && deltaZ == 2 -> NorthNorthEast // 14
            deltaX == 0 && deltaZ == 2 -> North // 13
            deltaX == -1 && deltaZ == 2 -> NorthNorthWest // 12
            deltaX == -2 && deltaZ == 2 -> NorthWest // 11
            deltaX == 2 && deltaZ == 1 -> EastNorthEast // 10
            deltaX == -2 && deltaZ == 1 -> WestNorthWest // 9
            deltaX == 2 && deltaZ == 0 -> East // 8
            deltaX == -2 && deltaZ == 0 -> West // 7
            deltaX == 2 && deltaZ == -1 -> EastSouthEast // 6
            deltaX == -2 && deltaZ == -1 -> WestSouthWest // 5
            deltaX == 2 && deltaZ == -2 -> SouthEast // 4
            deltaX == 1 && deltaZ == -2 -> SouthSouthEast // 3
            deltaX == 0 && deltaZ == -2 -> South // 2
            deltaX == -1 && deltaZ == -2 -> SouthSouthWest // 1
            deltaX == -2 && deltaZ == -2 -> SouthWest // 0
            // 8 point direction.
            deltaX == 1 && deltaZ == 1 -> NorthEast // 7
            deltaX == 0 && deltaZ == 1 -> North // 6
            deltaX == -1 && deltaZ == 1 -> NorthWest // 5
            deltaX == 1 && deltaZ == 0 -> East // 4
            deltaX == -1 && deltaZ == 0 -> West // 3
            deltaX == 1 && deltaZ == -1 -> SouthEast // 2
            deltaX == 0 && deltaZ == -1 -> South // 1
            deltaX == -1 && deltaZ == -1 -> SouthWest // 0
            else -> throw IllegalArgumentException("Could not find direction for deltaX $deltaX and deltaZ $deltaZ.")
        }
    }
}
