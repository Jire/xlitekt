package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.ObjDelPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPacketAssembler<ObjDelPacket>(opcode = 26, size = 3) {
    buildPacket {
        writeByteAdd { packedOffset }
        writeShortLittleEndian { id }
    }
}
