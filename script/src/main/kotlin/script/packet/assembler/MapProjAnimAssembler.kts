package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.MapProjAnimPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPacketAssembler<MapProjAnimPacket>(opcode = 64, size = 15) {
    buildPacket {
        writeByteAdd { packedOffset } // packedOffset
        writeShortLittleEndian { -1 } // targetIndex
        writeByteNegate { distanceX } // ?
        writeShort { lifespan } // endCycle
        writeByte { startHeight } // startHeight
        writeShortLittleEndian { id }
        writeByte { steepness } // ?
        writeByteNegate { angle } // slope
        writeShortLittleEndianAdd { delay } // startCycle
        writeByte { endHeight } // endHeight
        writeByteAdd { distanceZ } // ?
    }
}
