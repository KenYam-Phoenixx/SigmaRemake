package info.opensigma.command.impl

import info.opensigma.command.type.ChatCommandArguments
import info.opensigma.command.type.ChatCommandExecutor
import info.opensigma.command.type.Command

class Bind : Command("bind", "Bind a modul to a key", "b") {
    override fun run(argument: String, arguments: Array<ChatCommandArguments>, executor: ChatCommandExecutor) {
        TODO("Not yet implemented")
    }
}