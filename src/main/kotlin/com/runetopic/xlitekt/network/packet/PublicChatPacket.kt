package com.runetopic.xlitekt.network.packet

data class PublicChatPacket(
    val unknown: Int,
    val color: Int,
    val effect: Int,
    val size: Int,
    val data: ByteArray
) : Packet {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PublicChatPacket

        if (unknown != other.unknown) return false
        if (color != other.color) return false
        if (effect != other.effect) return false
        if (size != other.size) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = unknown
        result = 31 * result + color
        result = 31 * result + effect
        result = 31 * result + size.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
