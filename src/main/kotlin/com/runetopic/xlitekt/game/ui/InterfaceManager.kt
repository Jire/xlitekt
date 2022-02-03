package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.event.impl.IfEvent
import com.runetopic.xlitekt.network.packet.IfCloseSubPacket
import com.runetopic.xlitekt.network.packet.IfMoveSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.IfSetEventsPacket
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.RunClientScriptPacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.plugin.ktor.inject
import com.runetopic.xlitekt.util.ext.packInterface

/**
 * @author Tyler Telis
 */
class InterfaceManager(
    private val player: Player
) {
    private val open = mutableMapOf<Int, Int>()
    private val eventBus by inject<EventBus>()

    var displayMode = DisplayMode.FIXED

    fun login() {
        player.client.writePacket(VarpSmallPacket(1737, -1))
        player.client.writePacket(MessageGamePacket(0, "Welcome to Xlitekt.", false))
        openTop(displayMode.interfaceId)
        sendInterfacesForDisplayMode(displayMode)
    }

    fun switchDisplayMode(mode: DisplayMode) {
        val previousMode = this.displayMode
        this.displayMode = mode
        openTop(displayMode.interfaceId)
        runClientScript(3998, listOf(displayMode.mode - 1))
        InterfaceInfo.values().forEach {
            val fromInterfaceId = previousMode.interfaceId
            val fromChildId = it.componentIdForDisplay(previousMode)
            if (fromChildId == -1) return@forEach
            val toInterfaceId = mode.interfaceId
            val toChildId = it.componentIdForDisplay(mode)
            if (toChildId == -1) return@forEach

            moveSub(fromInterfaceId, fromChildId, toInterfaceId, toChildId)
        }
    }

    private fun moveSub(fromInterfaceId: Int, fromChildId: Int, toInterfaceId: Int, toChildId: Int): Boolean {
        val packedFromInterface = fromInterfaceId.packInterface(fromChildId)
        val packedToInterface = toInterfaceId.packInterface(toChildId)
        if (open.containsKey(packedFromInterface)) {
            open(packedToInterface, open[packedFromInterface]!!)
            player.client.writePacket(IfMoveSubPacket(packedFromInterface, packedToInterface))
            return true
        }
        return false
    }

    private fun sendInterfacesForDisplayMode(displayMode: DisplayMode) {
        InterfaceInfo.values().forEach {
            val interfaceId = it.interfaceId
            if (interfaceId == -1) return@forEach
            val componentId = it.componentIdForDisplay(displayMode)
            if (componentId == -1) return@forEach
            openSub(it.interfaceId, componentId, true)
        }
    }

    private fun open(packed: Int, interfaceId: Int): Boolean {
        if (open.containsKey(packed)) return false
        open[packed] = interfaceId
        return true
    }

    private fun openTop(interfaceId: Int) {
        val packed = interfaceId.packInterface()
        if (open(packed, interfaceId)) {
            player.client.writePacket(IfOpenTopPacket(interfaceId))
            eventBus.notify(IfEvent.IfOpenTopEvent(interfaceId))
        }
    }

    private fun openSub(interfaceId: Int, childId: Int, alwaysOpen: Boolean) {
        val packed = displayMode.interfaceId.packInterface(childId)
        if (open(packed, interfaceId)) {
            player.client.writePacket(
                IfOpenSubPacket(
                    interfaceId,
                    packed, alwaysOpen
                )
            )
            eventBus.notify(
                IfEvent.IfOpenSubEvent(
                    interfaceId,
                    childId,
                    alwaysOpen
                )
            )
        }
    }

    private fun closeSub(packedInterface: Int) {
        open.remove(packedInterface)
        player.client.writePacket(IfCloseSubPacket(packedInterface))
    }

    fun sendInterfaceEvent(packedInterface: Int, fromSlot: Int, toSlot: Int, events: Int) {
        player.client.writePacket(
            IfSetEventsPacket(
                packedInterface,
                fromSlot,
                toSlot,
                events
            )
        )
    }

    fun runClientScript(scriptId: Int, parameters: List<Any>) = player.client.writePacket(RunClientScriptPacket(scriptId, parameters))
}
