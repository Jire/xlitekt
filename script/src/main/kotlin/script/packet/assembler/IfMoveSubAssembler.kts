package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.IfMoveSubPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeInt

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfMoveSubPacket>(opcode = 30, size = 8) {
    buildPacket {
        writeInt { fromPackedInterface }
        writeInt { toPackedInterface }
    }
}
