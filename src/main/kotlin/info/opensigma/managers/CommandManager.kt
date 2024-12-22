package info.opensigma.managers

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket
import info.opensigma.command.impl.*
import info.opensigma.command.type.ChatCommandArguments
import info.opensigma.command.type.Command
import info.opensigma.util.addMessage
import info.opensigma.util.chatHud
import info.opensigma.util.mc
import net.minecraft.command.CommandException
import net.minecraft.text.Text
import java.util.*
import kotlin.collections.toTypedArray

class CommandManager {
    companion object {
        private val runnables = mutableListOf<Runnable>()
        const val CHAT_COMMAND_CHAR = "."
        const val CHAT_PREFIX = "§f[§6Sigma§f]§7"
    }

    private var useDefaultPrefix = true
    private val commands = mutableListOf<Command>()

    fun init() {
//        TODO
//        OpenSigma.instance.eventBus.isListening(this)
        register(VClip())
        register(HClip())
        register(Damage())
        register(ClearChat())
        register(EntityDesync())
        register(Peek())
        register(Insult())
        register(Bind())
        register(Help())
        register(Friend())
        register(Enemy())
        register(Toggle())
        register(Config())
        register(Panic())
        register(HighDPI())
        register(KillPotion())
        register(Enchant())
        register(TP())
    }

    private fun register(command: Command) {
        this.commands.add(command)
    }

    fun getCommands(): List<Command> {
        return this.commands
    }

    fun getCommandByName(name: String): Command? {
        for (command in this.commands) {
            if (command.name.equals(name, ignoreCase = true)) {
                return command
            }
        }

        for (var5 in this.commands) {
            for (var9 in var5.alias) {
                if (var9.equals(name, ignoreCase = true)) {
                    return var5
                }
            }
        }

        return null
    }

    fun invalidCommandHandler(name: String) {
        mc.chatHud.addMessage(Text.of(getPrefix() + " Invalid command \"" + CHAT_COMMAND_CHAR + name + "\""))
        mc.chatHud.addMessage(Text.of(getPrefix() + " Use \"" + CHAT_COMMAND_CHAR + "help\" for a list of commands."))
    }

    private fun getPrefix(): String {
        if (this.useDefaultPrefix) {
            this.useDefaultPrefix = false
            return CHAT_PREFIX
        } else {
            var result = ""

            for (_ in 0..7) {
                result = "$result "
            }

            return "$result§7"
        }
    }

    fun setUseDefaultPrefix() {
        this.useDefaultPrefix = true
    }

    @EventTarget
    private fun runRunnables(var1: TickEvent) {
        for (var5 in runnables) {
            var5.run()
        }

        runnables.clear()
    }

    @EventTarget
    private fun onSendPacket(var1: SendPacketEvent) {
//        if (OpenSigma.instance.clientMode != ClientMode.NOADDONS) {
            if (var1.packet is ChatMessageC2SPacket) {
                val var4 = var1.packet as ChatMessageC2SPacket
                val var5 = var4.message
                if (var5.startsWith(CHAT_COMMAND_CHAR, ignoreCase = true) && var5.substring(1).startsWith(CHAT_COMMAND_CHAR, ignoreCase = true)) {
                    var4.chatMessage = var5.substring(1)
                    return
                }

                if (var5.startsWith(CHAT_COMMAND_CHAR, ignoreCase = true)) {
                    var1.setCancelled(true)
                    this.setUseDefaultPrefix()
                    val args = var5.substring(CHAT_COMMAND_CHAR.length).split(" ".toRegex()).toTypedArray()
                    val cmd = this.getCommandByName(args[0])
                    if (cmd == null) {
                        this.invalidCommandHandler(args[0])
                        return
                    }

                    val var8: MutableList<ChatCommandArguments> = mutableListOf()

                    for (i in 1 until args.size) {
                        var8.add(ChatCommandArguments(args[i]))
                    }

                    mc.chatHud.addMessage(" ")

                    try {
                        cmd.run(var5, var8.toTypedArray(), { mc.chatHud.addChatMessage(this.getPrefix() + " " + it) })
                    } catch (exception: CommandException) {
                        if (exception.message?.isNotEmpty() == true) {
                            mc.chatHud.addMessage(this.getPrefix() + " Error: " + exception.message)
                        }

                        mc.chatHud.addMessage(this.getPrefix() + " Usage: " + "." + cmd.name + " " + cmd.method18326());
                    }

                    mc.chatHud.addMessage(" ");
                }
            }

            if (var1.getPacket() instanceof CTabCompletePacket) {
                CTabCompletePacket var11 = (CTabCompletePacket) var1.getPacket();
                if (var11.getCommand().startsWith(".")) {
                    var1.setCancelled(true);
                }
            }
//        }
    }
}