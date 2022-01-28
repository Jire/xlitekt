package com.runetopic.xlitekt.game.actor

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.player.Player

private const val MAX_PLAYER_COUNT = 2048
private const val MAX_NPC_COUNT = 32767

private const val INVALID_INDEX = -1
private const val INDEX_PADDING = 1

private inline fun <reified T> createList(count: Int): MutableList<T?> =
    arrayOfNulls<T>(count).toMutableList()

private inline fun <reified T> List<T>.freeIndex(): Int {
    for (i in INDEX_PADDING until indices.last) {
        if (this[i] == null) {
            return i
        }
    }
    return INVALID_INDEX
}

class PlayerList(
    private val players: MutableList<Player?> = createList(MAX_PLAYER_COUNT)
) : List<Player?> by players {

    override val size: Int
        get() = players.count { it != null }

    val indices: IntRange
        get() = players.indices

    val capacity: Int
        get() = players.size

    fun register(player: Player): Boolean {
        val index = players.freeIndex()
        if (index == INVALID_INDEX) {
            return false
        }
        players[index] = player
        player.index = index
        return true
    }

    fun remove(player: Player): Boolean = when {
        player.index == INVALID_INDEX -> false
        players[player.index] != player -> false
        else -> {
            players[player.index] = null
            true
        }
    }

    override fun isEmpty(): Boolean = size == 0
}

class NpcList(
    private val npcs: MutableList<NPC?> = createList(MAX_NPC_COUNT)
) : List<NPC?> by npcs {

    override val size: Int
        get() = npcs.count { it != null }

    val indices: IntRange
        get() = npcs.indices

    val capacity: Int
        get() = npcs.size

    fun register(npc: NPC): Boolean {
        val index = npcs.freeIndex()
        if (index == INVALID_INDEX) {
            return false
        }
        npcs[index] = npc
        npc.index = index
        return true
    }

    fun remove(npc: NPC): Boolean = when {
        npc.index == INVALID_INDEX -> false
        npcs[npc.index] != npc -> false
        else -> {
            npcs[npc.index] = null
            true
        }
    }

    override fun isEmpty(): Boolean = size == 0
}
