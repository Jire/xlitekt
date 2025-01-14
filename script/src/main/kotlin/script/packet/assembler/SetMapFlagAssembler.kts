package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.SetMapFlagPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<SetMapFlagPacket>(opcode = 93, size = 2) {
    buildPacket {
        writeByte { destinationX }
        writeByte { destinationZ }
    }
}
