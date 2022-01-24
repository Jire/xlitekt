package com.runetopic.xlitekt.network.client

import com.runetopic.cryptography.isaac.ISAAC
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.handler.EventHandler
import com.runetopic.xlitekt.network.handler.HandshakeEventHandler
import com.runetopic.xlitekt.network.packet.Packet
import com.runetopic.xlitekt.network.pipeline.EventPipeline
import com.runetopic.xlitekt.network.pipeline.GameEventPipeline
import com.runetopic.xlitekt.network.pipeline.HandshakeEventPipeline
import com.runetopic.xlitekt.plugin.ktor.inject
import io.ktor.network.sockets.Socket
import io.ktor.util.reflect.instanceOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import kotlinx.coroutines.Dispatchers
import org.slf4j.Logger

class Client(
    private val socket: Socket,
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel
) {
    private var eventPipeline: EventPipeline<ReadEvent, WriteEvent> = useEventPipeline(inject<HandshakeEventPipeline>())
    private var eventHandler: EventHandler<ReadEvent, WriteEvent> = useEventHandler(inject<HandshakeEventHandler>())

    var clientCipher: ISAAC? = null
    var serverCipher: ISAAC? = null
    private var connected: Boolean = true
    val seed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
    var connectedToJs5 = false
    var loggedIn = false

    private val logger by inject<Logger>()

    suspend fun startIOEvents() {
        while (connected) {
            try {
                println("OK")
                if (eventPipeline.instanceOf(GameEventPipeline::class)) {
                    eventPipeline.read(this)?.let {
                        eventHandler.handleEvent(this, it)
                    }
                } else {
                    eventPipeline.read(this)?.let { read ->
                        eventHandler.handleEvent(this, read)?.let { write ->
                            eventPipeline.write(this, write)
                        } ?: disconnect()
                    } ?: disconnect()
                }
            } catch (exception: Exception) {
                inject<Logger>().value.error("Exception caught during client IO Events.", exception)
                disconnect()
            }
        }
    }

    suspend fun writeResponse(response: Int) {
        writeChannel.writeByte(response.toByte())
        writeChannel.flush()
    }

    fun setIsaacCiphers(clientCipher: ISAAC, serverCipher: ISAAC) {
        if (this.clientCipher != null || this.serverCipher != null) {
            disconnect()
            return
        }
        this.clientCipher = clientCipher
        this.serverCipher = serverCipher
    }

    suspend fun writePacket(packet: Packet) = eventPipeline.write(this, WriteEvent.GameWriteEvent(packet.opcode(), packet.size(), packet.builder().build()))

    fun disconnect() {
        connected = false
        socket.close()
        logger.info("Client disconnected.")
    }

    @Suppress("UNCHECKED_CAST")
    fun useEventPipeline(eventPipeline: Lazy<EventPipeline<out ReadEvent, out WriteEvent>>): EventPipeline<ReadEvent, WriteEvent> {
        this.eventPipeline = eventPipeline.value as EventPipeline<ReadEvent, WriteEvent>
        return this.eventPipeline
    }

    @Suppress("UNCHECKED_CAST")
    fun useEventHandler(eventHandler: Lazy<EventHandler<*, *>>): EventHandler<ReadEvent, WriteEvent> {
        this.eventHandler = eventHandler.value as EventHandler<ReadEvent, WriteEvent>
        return this.eventHandler
    }
}
