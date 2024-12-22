package info.opensigma.command.impl

import info.opensigma.command.type.ChatCommandArguments
import info.opensigma.command.type.ChatCommandExecutor
import info.opensigma.command.type.Command
import info.opensigma.command.type.CommandType
import info.opensigma.util.mc
import info.opensigma.util.network
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket

class VClip : Command("vclip", "Vertical clip through blocks", "vc") {
    init {
        registerSubCommands("offset")
    }

    override fun run(argument: String, arguments: Array<ChatCommandArguments>, executor: ChatCommandExecutor) {
        if (arguments.isNotEmpty()) {
            if (arguments.size == 1) {
                if (arguments[0].getCommandType() != CommandType.NUMBER) {
                    throw CommandException("Invalid vertical distance \"${arguments[0].arguments}\"")
                } else {
                    network.handlePlayerPosLook(
                        PlayerPositionLookS2CPacket(
                            mc.player!!.x,
                            mc.player!!.y + arguments[0].getDouble(),
                            mc.player!!.z,
                            mc.player!!.yaw,
                            mc.player!!.pitch,
                            emptySet(),
                            (2.147483647E9 * Math.random()).toInt()
                        )
                    )
                    executor.send("VClip'd to position ${mc.player!!.y + arguments[0].getDouble()}")
                }
            } else {
                throw CommandException("Too many arguments")
            }
        } else {
            throw CommandException()
        }
    }
}