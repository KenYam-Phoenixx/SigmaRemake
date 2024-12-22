package info.opensigma.managers

import info.opensigma.OpenSigma
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket
import info.opensigma.command.impl.*
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
        private val field38299 = mutableListOf<Runnable>()
        const val CHAT_COMMAND_CHAR = "."
        const val CHAT_PREFIX = "§f[§6Sigma§f]§7"
    }

    private var field38298 = true
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
        if (this.field38298) {
            this.field38298 = false
            return CHAT_PREFIX
        } else {
            var var3 = ""

            for (var4 in 0..7) {
                var3 = "$var3 "
            }

            return "$var3§7"
        }
    }

    fun method30236() {
        this.field38298 = true
    }

    @EventTarget
    private fun method30237(var1: TickEvent) {
        for (var5 in field38299) {
            var5.run()
        }

        field38299.clear()
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
                    this.method30236()
                    val var6 = var5.substring(CHAT_COMMAND_CHAR.length).split(" ".toRegex()).toTypedArray()
                    val var7 = this.getCommandByName(var6[0])
                    if (var7 == null) {
                        this.invalidCommandHandler(var6[0])
                        return
                    }

                    val var8: MutableList<ChatCommandArguments> = mutableListOf()

                    for (var9 in 1 until var6.size) {
                        var8.add(ChatCommandArguments(var6[var9]))
                    }

                    mc.chatHud.addMessage(" ")

                    try {
                        var7.run(var5, var8.toTypedArray(), { mc.chatHud.addChatMessage(this.getPrefix() + " " + it) })
                    } catch (var10: CommandException) {
                        if (var10.message?.isNotEmpty() == true) {
                            mc.chatHud.addMessage(this.getPrefix() + " Error: " + var10.message)
                        }

                        mc.chatHud.addMessage(this.getPrefix() + " Usage: " + "." + var7.getName() + " " + var7.method18326());
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