package info.opensigma.command.impl

import info.opensigma.command.type.ChatCommandArguments
import info.opensigma.command.type.ChatCommandExecutor
import info.opensigma.command.type.Command
import info.opensigma.command.type.CommandType
import net.minecraft.command.CommandException

class VClip : Command("vclip", "Vertical clip through blocks", "vc") {
    init {
        registerSubCommands("offset")
    }

    override fun run(argument: String, arguments: Array<ChatCommandArguments>, executor: ChatCommandExecutor) {
        if (var2.isNotEmpty()) {
            if (var2.size == 1) {
                if (var2[0].commandType != CommandType.NUMBER) {
                    throw CommandException("Invalid vertical distance \"${var2[0].arguments}\"")
                } else {
                    mc.connection!!.handlePlayerPosLook(
                        SPlayerPositionLookPacket(
                            mc.player!!.posX,
                            mc.player!!.posY + var2[0].method30896(),
                            mc.player!!.posZ,
                            mc.player!!.rotationYaw,
                            mc.player!!.rotationPitch,
                            emptySet(),
                            (2.147483647E9 * Math.random()).toInt()
                        )
                    )
                    executor.send("VClip'd to position ${mc.player!!.posY + var2[0].method30896()}")
                }
            } else {
                throw CommandException("Too many arguments")
            }
        } else {
            throw CommandException()
        }
    }
}