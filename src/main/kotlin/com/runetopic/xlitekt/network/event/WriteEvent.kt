package com.runetopic.xlitekt.network.event

import com.runetopic.xlitekt.game.player.Player
import io.ktor.utils.io.core.ByteReadPacket
import java.nio.ByteBuffer

sealed class WriteEvent {
    data class HandshakeWriteEvent(
        val opcode: Int,
        val response: Int
    ) : WriteEvent()

    data class JS5WriteEvent(
        val indexId: Int,
        val groupId: Int,
        val compression: Int,
        val size: Int,
        val bytes: ByteBuffer
    ) : WriteEvent() { constructor() : this(-1, -1, -1, -1, ByteBuffer.allocate(0)) }

    data class LoginWriteEvent(
        val response: Int,
        val rights: Int,
        val pid: Int,
        val player: Player
    ) : WriteEvent()

    data class GameWriteEvent(
        val opcode: Int,
        val size: Int,
        val payload: ByteReadPacket
    ) : WriteEvent()
}
