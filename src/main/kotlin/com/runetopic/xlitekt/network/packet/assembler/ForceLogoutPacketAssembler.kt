package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.ForceLogoutPacket

/**
 * @author Tyler Telis
 */
class ForceLogoutPacketAssembler : PacketAssembler<ForceLogoutPacket>(opcode = 87, size = 0) {
    override fun assemblePacket(packet: ForceLogoutPacket) = buildPacket {}
}
