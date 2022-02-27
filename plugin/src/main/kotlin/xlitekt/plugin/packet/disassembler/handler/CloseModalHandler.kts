package xlitekt.plugin.packet.disassembler.handler

import xlitekt.game.packet.CloseModalPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler

/**
 * @author Jordan Abraham
 */
onPacketHandler<CloseModalPacket> {
    player.interfaces.closeModal()
}
