package info.opensigma.command.type

import net.minecraft.client.MinecraftClient

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

    abstract fun run(argument: String, arguments: Array<ChatCommandArguments>, executor: ChatCommandExecutor): Unit
}