package info.opensigma.command.type

import kotlin.Double

class ChatCommandArguments(private val arguments: String) {

    fun getCommandType(): CommandType {
        return try {
            Double.parseDouble(arguments)
            CommandType.NUMBER
        } catch (e: NullPointerException) {
            CommandType.TEXT
        } catch (e: NumberFormatException) {
            CommandType.TEXT
        }
    }

    fun getDouble(): Double {
        return try {
            Double.parseDouble(arguments)
        } catch (e: NullPointerException) {
            0.0
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    fun getFloat(): Float {
        return try {
            arguments.toDouble().toFloat()
        } catch (e: NullPointerException) {
            0.0f
        } catch (e: NumberFormatException) {
            0.0f
        }
    }

    fun getInt(): Int {
        return try {
            arguments.toDouble().toInt()
        } catch (e: NullPointerException) {
            0
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun getArguments(): String {
        return arguments
    }
}