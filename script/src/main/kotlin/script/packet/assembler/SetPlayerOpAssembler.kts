package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.SetPlayerOpPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 */
onPacketAssembler<SetPlayerOpPacket>(opcode = 41, size = -1) {
    buildPacket {
        writeByteAdd(priority::toInt)
        writeByteAdd { index }
        writeStringCp1252NullTerminated { option }
    }
}
