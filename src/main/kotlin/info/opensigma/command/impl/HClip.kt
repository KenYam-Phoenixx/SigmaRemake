package info.opensigma.command.impl

import info.opensigma.command.type.ChatCommandArguments
import info.opensigma.command.type.ChatCommandExecutor
import info.opensigma.command.type.Command

class HClip : Command("hclip", "Horizontal Clip", "hc") {
    override fun run(
        argument: String,
        arguments: Array<ChatCommandArguments>,
        executor: ChatCommandExecutor
    ) {
        TODO("Not yet implemented")
    }

    init {
        registerSubCommands("offset")
    }
}