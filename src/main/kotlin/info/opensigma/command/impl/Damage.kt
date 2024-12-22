package info.opensigma.command.impl

import info.opensigma.command.type.ChatCommandArguments
import info.opensigma.command.type.ChatCommandExecutor
import info.opensigma.command.type.Command
import info.opensigma.command.type.CommandException
import info.opensigma.command.type.CommandType
import info.opensigma.util.network
import info.opensigma.util.player
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

class Damage : Command("damage", "Damages you", "dmg") {
    init {
        this.registerSubCommands("hearts")
    }
    override fun run(argument: String, arguments: Array<ChatCommandArguments>, executor: ChatCommandExecutor) {
    if (arguments.isEmpty()) {
        throw CommandException()
    } else if (arguments.size > 1) {
        throw CommandException("Too many arguments")
    } else if (arguments[0].getCommandType() != CommandType.NUMBER) {
        throw CommandException("Invalid heart damage amount \"${arguments[0].getArguments()}\"")
    } else {
        for (i in 0 until (80.0 + 40.0 * (arguments[0].getInt() - 0.5)).toInt()) {
            network.sendPacket(
                PlayerMoveC2SPacket.PositionOnly(
                    player.x, player.y + 0.06,
                    player.z, false
                )
            )
            network.sendPacket(
                PlayerMoveC2SPacket.PositionOnly(
                    player.x, player.y,
                    player.z, false
                )
            )
        }

        network.sendPacket(
            PlayerMoveC2SPacket.PositionOnly(
                player.x, player.y,
                player.z, false
            )
        )
        network.sendPacket(
            PlayerMoveC2SPacket.PositionOnly(
                player.x, player.y + 0.02,
                player.z, false
            )
        )
        executor.send("Sent damage packets")
    }
}
}