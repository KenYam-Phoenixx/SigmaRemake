package info.opensigma.command.type

import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandException

abstract class Command(val name: String, val descriptor: String, vararg val alias: String) {
    private val subCommands = mutableListOf<String>()

    val minecraft: MinecraftClient
        get() = MinecraftClient.getInstance()

    fun getSubCommands(): List<String> {
        return subCommands
    }

    fun registerSubCommands(vararg subCommands: String) {
        this.subCommands.addAll(subCommands)
    }

    @Throws(CommandException::class)
    abstract fun run(argument: String, arguments: Array<ChatCommandArguments>, executor: ChatCommandExecutor)
}