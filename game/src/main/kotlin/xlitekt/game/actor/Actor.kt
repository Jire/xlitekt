package xlitekt.game.actor

import xlitekt.game.actor.movement.Movement
import xlitekt.game.actor.movement.MovementSpeed
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.movement.isValid
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.sendRebuildNormal
import xlitekt.game.actor.player.shouldRebuildMap
import xlitekt.game.actor.render.HitBarType
import xlitekt.game.actor.render.HitDamage
import xlitekt.game.actor.render.HitType
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.Render.Appearance
import xlitekt.game.actor.render.Render.FaceActor
import xlitekt.game.actor.render.Render.FaceAngle
import xlitekt.game.actor.render.Render.Hit
import xlitekt.game.actor.render.Render.MovementType
import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.Render.SpotAnimation
import xlitekt.game.actor.render.Render.TemporaryMovementType
import xlitekt.game.actor.render.block.HighDefinitionRenderingBlock
import xlitekt.game.actor.render.block.LowDefinitionRenderingBlock
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.game.world.map.location.Location
import java.util.TreeMap

abstract class Actor(
    open var location: Location
) {
    private val movement = Movement()

    private val highDefinitionRenderingBlocks = TreeMap<Int, HighDefinitionRenderingBlock>()
    private val lowDefinitionRenderingBlocks = TreeMap<Int, LowDefinitionRenderingBlock>()

    var previousLocation: Location? = null
    var index = 0

    private var facingActorIndex = -1

    abstract fun totalHitpoints(): Int
    abstract fun currentHitpoints(): Int

    /**
     * Routes the actor movement waypoints to the input list.
     */
    fun route(locations: List<Location>) {
        actionReset()
        movement.route(locations)
    }

    /**
     * Routes the actor movement to a single waypoint with optional teleport speed.
     */
    fun route(location: Location, teleport: Boolean = false) {
        actionReset()
        movement.route(location, teleport)
    }

    /**
     * Toggles the actor movement speed between walking and running.
     * If the actor is a Player then this will also flag for movement and temporary movement type updates.
     */
    fun toggleMovementSpeed() {
        movement.movementSpeed = if (movement.movementSpeed.isRunning()) MovementSpeed.WALKING else MovementSpeed.RUNNING
        if (this is Player) {
            movementType(movement.movementSpeed::isRunning)
            temporaryMovementType(movement.movementSpeed::id)
        }
    }

    /**
     * Processes any pending movement this actor may have. This happens every tick.
     */
    fun processMovement(players: Map<Int, Player>): MovementStep = movement.process(this, location).also {
        if (this is Player) {
            if (facingActorIndex != -1 && !it.isValid()) {
                faceActor { -1 }
            }
            if (it.isValid() && shouldRebuildMap()) sendRebuildNormal(false, players)
        }
    }

    /**
     * Returns if this actor has any high definition rendering blocks.
     */
    fun hasHighDefinitionRenderingBlocks() = highDefinitionRenderingBlocks.isNotEmpty()

    /**
     * Returns a list of this actors high definition rendering blocks.
     */
    fun highDefinitionRenderingBlocks() = highDefinitionRenderingBlocks.values.toList()

    /**
     * Adds a high definition rendering block to a low definition one.
     */
    fun addLowDefinitionRenderingBlock(highDefinitionRenderingBlock: HighDefinitionRenderingBlock, block: ByteArray) {
        val lowDefinitionRenderingBlock = LowDefinitionRenderingBlock(
            render = highDefinitionRenderingBlock.render,
            mask = highDefinitionRenderingBlock.renderingBlock.mask,
            block = block
        )
        // Insert the rendering block into the TreeMap based on its index. This is to preserve order based on the client.
        lowDefinitionRenderingBlocks[highDefinitionRenderingBlock.renderingBlock.index] = lowDefinitionRenderingBlock
    }

    /**
     * Returns a list of this actors low definition rendering blocks.
     */
    fun lowDefinitionRenderingBlocks() = lowDefinitionRenderingBlocks.values.toList()

    /**
     * Happens after this actor has finished processing by the game loop.
     */
    fun postSync() {
        // Clear the high definition blocks.
        highDefinitionRenderingBlocks.clear()
        // We only want to persist these types of low definition blocks.
        lowDefinitionRenderingBlocks.values.removeIf {
            it.render::class != Appearance::class &&
                it.render::class != FaceAngle::class &&
                it.render::class != MovementType::class
        }
    }

    /**
     * Use this when the player does an action. This will need work.
     */
    private fun actionReset() {
        movement.reset()
        if (facingActorIndex != -1 && this is Player) {
            faceActor { -1 }
            facingActorIndex = -1
        }
    }

    /**
     * Flags this actor with a new pending rendering block.
     */
    fun render(render: Render) {
        val renderingBlock = PlayerRenderingBlockListener.listeners[render::class] ?: return
        // Insert the rendering block into the TreeMap based on its index. This is to preserve order based on the client.
        highDefinitionRenderingBlocks[renderingBlock.index] = HighDefinitionRenderingBlock(render, renderingBlock)
        when (render) {
            is FaceActor -> facingActorIndex = render.index
            else -> {} // TODO
        }
    }
}

inline fun Actor.faceActor(index: () -> Int) {
    render(FaceActor(index.invoke()))
}

inline fun Actor.faceAngle(angle: () -> Int) {
    render(FaceAngle(angle.invoke()))
}

inline fun Actor.animate(sequenceId: () -> Int) {
    render(Sequence(sequenceId.invoke()))
}

inline fun Actor.spotAnimate(spotAnimationId: () -> Int) {
    render(SpotAnimation(spotAnimationId.invoke()))
}

inline fun Actor.movementType(running: () -> Boolean) {
    render(MovementType(running.invoke()))
}

inline fun Actor.temporaryMovementType(id: () -> Int) {
    render(TemporaryMovementType(id.invoke()))
}

inline fun Actor.hit(hitBarType: HitBarType, source: Actor?, type: HitType, delay: Int, damage: () -> Int) {
    render(
        Hit(
            actor = this,
            hits = listOf(HitDamage(source, type, damage.invoke(), delay)),
            bars = listOf(hitBarType)
        )
    )
}

inline fun Actor.chat(rights: Int, effects: Int, message: () -> String) {
    render(Render.PublicChat(message.invoke(), effects, rights))
}
