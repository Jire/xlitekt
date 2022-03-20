package xlitekt.game.loop.task

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.actor.Actor
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.movement.isValid
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.sendRebuildNormal
import xlitekt.game.actor.player.shouldRebuildMap
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 */
abstract class Task : Runnable {
    protected fun Actor.processMovement(): MovementStep = movement.process(location).also {
        if (this is Player) {
            if (it.isValid() && shouldRebuildMap()) sendRebuildNormal(false)
        }
    }

    protected fun Player.processUpdateBlocks(pending: List<Render>): ByteReadPacket {
        if (pending.isEmpty()) return ByteReadPacket.Empty
        return pending.buildPlayerUpdateBlocks(this)
    }

    protected fun Player.sync(
        updates: Map<Player, ByteReadPacket>,
        previousLocations: Map<Player, Location?>,
        locations: Map<Player, Location>,
        playerSteps: Map<Player, MovementStep?>,
        npcSteps: Map<NPC, MovementStep>
    ) {
        write(PlayerInfoPacket(viewport, updates, previousLocations, locations, playerSteps))
        write(NPCInfoPacket(viewport, locations, npcSteps))
        flushPool()
        reset()
    }
}
