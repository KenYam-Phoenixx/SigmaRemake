package info.opensigma.command.impl

import info.opensigma.command.type.ChatCommandArguments
import info.opensigma.command.type.ChatCommandExecutor
import info.opensigma.command.type.Command
import net.minecraft.client.MinecraftClient

class ClearChat : Command("clear", "Clears your chat client side", "cc", "chatclear") {
    override fun run(argument: String, arguments: Array<ChatCommandArguments>, executor: ChatCommandExecutor) {
        MinecraftClient.getInstance().inGameHud.chatHud.clear(true)
    }
}