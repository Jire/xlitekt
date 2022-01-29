package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeByteNegate
import com.runetopic.xlitekt.util.ext.writeShortAdd
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class PlayerSequenceBlock : RenderingBlock<Player, Render.Animation>(8, 0x2) {

    override fun build(actor: Player, render: Render.Animation): ByteReadPacket = buildPacket {
        writeShortAdd(render.id.toShort())
        writeByteNegate(render.delay.toByte())
    }
}
