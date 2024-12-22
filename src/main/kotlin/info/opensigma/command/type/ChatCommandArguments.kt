package info.opensigma.command.type

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

    fun method30896(): Double {
        return try {
            Double.parseDouble(arguments)
        } catch (e: NullPointerException) {
            0.0
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    fun method30897(): Float {
        return try {
            arguments.toDouble().toFloat()
        } catch (e: NullPointerException) {
            0.0f
        } catch (e: NumberFormatException) {
            0.0f
        }
    }

    fun method30898(): Int {
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