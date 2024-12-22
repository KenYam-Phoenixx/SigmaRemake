package info.opensigma.command.impl

import info.opensigma.OpenSigma
import info.opensigma.command.type.ChatCommandArguments
import info.opensigma.command.type.ChatCommandExecutor
import info.opensigma.command.type.Command
import info.opensigma.command.type.CommandType
import kotlinx.io.IOException
import java.io.File

class Config : Command("config", "Manage configs", "configs", "profiles", "profile") {
    init {
        this.registerSubCommands("load", "save", "remove", "list")
    }

    override fun run(argument: String, arguments: Array<ChatCommandArguments>, executor: ChatCommandExecutor) {
        if (arguments.isEmpty()) {
            throw CommandException()
        } else if (arguments.size <= 2) {
            if (arguments[0].commandType != CommandType.TEXT) {
                throw CommandException()
            } else {
                val action = arguments[0].arguments.toLowerCase()

                when {
                    action.equals("load", ignoreCase = true) -> {
                        if (arguments.size != 2) {
                            executor.send("Usage : .config load <name>")
                        } else {
                            val name = arguments[1].arguments.toLowerCase()
                            val config = OpenSigma.instance.configManager.getConfigByName(name)
                            if (config == null) {
                                executor.send("${this.getConfigOrProfileName()} not found!")
                            } else {
                                OpenSigma.instance.configManager.loadConfig(config)
                                executor.send("${this.getConfigOrProfileName()} was loaded!")
                            }
                        }
                    }
                    action in "saveCommands" -> {
                        if (arguments.size != 2) {
                            executor.send("Usage : .config save <name>")
                        } else {
                            val name = arguments[1].arguments.toLowerCase()
                            val ogName = arguments[1].arguments
                            val currentConfig = OpenSigma.instance.configManager.currentConfig
                            currentConfig.serializedConfigData = OpenSigma.instance.moduleManager.saveCurrentConfigToJSON(JSONObject())
                            OpenSigma.instance.configManager.removeConfig(name)
                            OpenSigma.instance.configManager.saveConfig(Configuration(name, currentConfig.serializedConfigData))
                            executor.send("Saved ${this.getConfigOrProfileName()}")
                        }
                    }
                    action in "deleteCommands" -> {
                        if (arguments.size != 2) {
                            executor.send("Usage : .config remove <name>")
                        } else {
                            val name = arguments[1].arguments.toLowerCase()
                            if (!OpenSigma.instance.configManager.removeConfig(name)) {
                                executor.send("${this.getConfigOrProfileName()} not found!")
                            } else {
                                executor.send("Removed ${this.getConfigOrProfileName()}")
                            }
                        }
                    }
                    action.equals("list", ignoreCase = true) -> {
                        executor.send("§l${OpenSigma.instance.configManager.allConfigs.size} ${this.getConfigOrProfileName()} :")

                        for (config in OpenSigma.instance.configManager.allConfigs) {
                            val isCurrentConfig = OpenSigma.instance.configManager.currentConfig == config
                            if (OpenSigma.instance.OpenSigmaMode != OpenSigmaMode.CLASSIC || !isCurrentConfig) {
                                executor.send("${if (!isCurrentConfig) "" else "§n"}${config.name}")
                            }
                        }
                    }
                    else -> throw CommandException()
                }
            }
        } else {
            throw CommandException("Too many arguments")
        }
    }
    fun getConfigOrProfileName(): String {
        return "Config"
//        return if (OpenSigma.instance.clientMode != ClientMode.CLASSIC) "Profile" else "Config"
    }

    fun saveConfigToFile(configName: String) {
        val jsonConfig = OpenSigma.instance.configManager.saveCurrentConfigToJSON(JSONObject())
        val configFolder = File(OpenSigma.instance.file + this.configFolder)
        if (!configFolder.exists()) {
            configFolder.mkdirs()
        }

        val configFile = File(OpenSigma.instance.file + this.configFolder + configName + this.configFileExtension)
        if (!configFile.exists()) {
            configFile.createNewFile()
        }

        try {
            configFile.writeText(jsonConfig.toString(0))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}