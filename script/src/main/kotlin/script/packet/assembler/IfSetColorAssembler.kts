package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.IfSetColorPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfSetColorPacket>(opcode = 62, size = 6) {
    buildPacket {
        writeIntV2 { packedInterface }
        writeShortLittleEndian { color }
    }
}
