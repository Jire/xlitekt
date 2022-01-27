package com.runetopic.xlitekt.network.packet

/**
 * @author Jordan Abraham
 */
data class SoundEffectPacket(
    val id: Int,
    val count: Int,
    val delay: Int
) : Packet
