package info.opensigma.command.impl

import info.opensigma.command.type.Command

class HClip : Command("hclip", "Horizontal Clip", "hc") {
    init {
        registerSubCommands("offset")
    }
}