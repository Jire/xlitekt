package com.runetopic.xlitekt.network.packet.handler

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.cache.Cache.entryType
import com.runetopic.xlitekt.cache.provider.ui.InterfaceEntryType
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.event.impl.IfEvent
import com.runetopic.xlitekt.network.packet.IfButtonPacket
import com.runetopic.xlitekt.plugin.koin.inject

/**
 * @author Jordan Abraham
 */
class IfButtonPacketHandler : PacketHandler<IfButtonPacket> {

    private val logger = InlineLogger()
    private val eventBus by inject<EventBus>()

    override suspend fun handlePacket(player: Player, packet: IfButtonPacket) {
        val index = packet.index
        val interfaceId = packet.packedInterface shr 16
        val childId = packet.packedInterface and 0xffff
        val slotId = packet.slotId
        val itemId = packet.itemId
        val entry = entryType<InterfaceEntryType>(packet.packedInterface)
        eventBus.notify(
            IfEvent.IfButtonClickEvent(
                player,
                index = index,
                interfaceId = interfaceId,
                option = entry?.actions?.firstOrNull() ?: "*",
                childId = childId,
                slotId = slotId,
                itemId = itemId
            )
        )
        logger.debug { "Notifying event bus with interfaceId=$interfaceId, childId=$childId, slotId=$slotId, itemId=$itemId, index=$index" }
    }
}
