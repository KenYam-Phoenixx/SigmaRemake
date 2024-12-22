package info.opensigma.managers

//import com.mentalfrostbyte.jello.Client
//import com.mentalfrostbyte.jello.ClientMode
//import com.mentalfrostbyte.jello.command.impl.*
//import com.mentalfrostbyte.jello.event.EventTarget
//import com.mentalfrostbyte.jello.event.impl.SendPacketEvent
//import com.mentalfrostbyte.jello.event.impl.TickEvent
//import com.mentalfrostbyte.jello.util.MultiUtilities
//import mapped.*
//import net.minecraft.network.play.client.CChatMessagePacket
//import net.minecraft.network.play.client.CTabCompletePacket

import info.opensigma.command.impl.Bind
import info.opensigma.command.impl.ClearChat
import info.opensigma.command.impl.Config
import info.opensigma.command.impl.Damage
import info.opensigma.command.impl.Enchant
import info.opensigma.command.impl.Enemy
import info.opensigma.command.impl.EntityDesync
import info.opensigma.command.impl.Friend
import info.opensigma.command.impl.HClip
import info.opensigma.command.impl.Help
import info.opensigma.command.impl.HighDPI
import info.opensigma.command.impl.Insult
import info.opensigma.command.impl.KillPotion
import info.opensigma.command.impl.Panic
import info.opensigma.command.impl.Peek
import info.opensigma.command.impl.TP
import info.opensigma.command.impl.Toggle
import info.opensigma.command.impl.VClip
import info.opensigma.command.type.Command
import java.util.*

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

    fun method30231(var1: String): Command? {
        for (var5 in this.commands) {
            if (var5.name.equals(var1, ignoreCase = true)) {
                return var5
            }
        }

        for (var5 in this.commands) {
            for (var9 in var5.alias) {
                if (var9.equals(var1, ignoreCase = true)) {
                    return var5
                }
            }
        }

        return null
    }

    fun method30234(var1: String) {
        MultiUtilities.addChatMessage(getPrefix() + " Invalid command \"" + CHAT_COMMAND_CHAR + var1 + "\"")
        MultiUtilities.addChatMessage(getPrefix() + " Use \"" + CHAT_COMMAND_CHAR + "help\" for a list of commands.")
    }

    private fun getPrefix(): String {
        if (this.field38298) {
            this.field38298 = false
            return CHAT_PREFIX
        } else {
            var var3 = ""

            for (var4 in 0..7) {
                var3 = var3 + " "
            }

            return var3 + "§7"
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
        if (Client.getInstance().clientMode != ClientMode.NOADDONS) {
            if (var1.packet is CChatMessagePacket) {
                val var4 = var1.packet as CChatMessagePacket
                val var5 = var4.message
                if (var5.startsWith(CHAT_COMMAND_CHAR, ignoreCase = true) && var5.substring(1).startsWith(CHAT_COMMAND_CHAR, ignoreCase = true)) {
                    var4.message = var5.substring(1)
                    return
                }

                if (var5.startsWith(CHAT_COMMAND_CHAR, ignoreCase = true)) {
                    var1.setCancelled(true)
                    this.method30236()
                    val var6 = var5.substring(CHAT_COMMAND_CHAR.length).split(" ".toRegex()).toTypedArray()
                    val var7 = this.method30231(var6[0])
                    if (var7 == null) {
                        this.method30234(var6[0])
                        return
                    }

                    val var8: MutableList<ChatCommandArguments> = mutableListOf()

                    for (var9 in 1 until var6.size) {
                        var8.add(ChatCommandArguments(var6[var9]))
                    }

                    MultiUtilities.addChatMessage(" ")

                    try {
                        var7.run(var5, var8.toTypedArray(), { MultiUtilities.addChatMessage(this.getPrefix() + " " + it) })
                    } catch (var10: CommandException) {
                        if (var10.message.length > 0) {
                            MultiUtilities.addChatMessage(this.getPrefix() + " Error: " + var10.message)
                        }

                        MultiUtilities.addChatMessage(this.getPrefix() + " Usage: " + "." + var7.getName() + " " + var7.method18326());
                    }

                    MultiUtilities.addChatMessage(" ");
                }
            }

            if (var1.getPacket() instanceof CTabCompletePacket) {
                CTabCompletePacket var11 = (CTabCompletePacket) var1.getPacket();
                if (var11.getCommand().startsWith(".")) {
                    var1.setCancelled(true);
                }
            }
        }
    }
}