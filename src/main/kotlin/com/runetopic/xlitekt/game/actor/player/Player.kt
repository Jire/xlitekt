package com.runetopic.xlitekt.game.actor.player

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.game.ui.InterfaceManager
import com.runetopic.xlitekt.game.varp.VarsManager
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.packet.RebuildNormalPacket
import com.runetopic.xlitekt.plugin.koin.inject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
class Player(
    val client: Client,
    val username: String,
) : Actor(Tile(3222, 3222)) {
    var appearance = Render.Appearance(Render.Appearance.Gender.MALE, -1, -1, -1, false)

    var rights = 2
    var online = false

    val viewport = Viewport(this)
    val interfaceManager = InterfaceManager(this)
    val varsManager = VarsManager(this)

    fun login() {
        this.previousTile = this.tile
        client.writePacket(RebuildNormalPacket(viewport, tile, true))
        refreshAppearance()
        interfaceManager.login()
        varsManager.login()
        // Set the player online here, so they start processing by the main game loop.
        online = true
    }

    fun logout() {
        online = false
        inject<World>().value.players.remove(this)
    }

    // TODO build appearance manager for changing gender and appearance related stuff
    fun refreshAppearance(appearance: Render.Appearance = this.appearance): Render.Appearance {
        this.appearance = renderer.appearance(appearance)
        return this.appearance
    }

    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100
}
