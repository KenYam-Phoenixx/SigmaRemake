package info.opensigma.command.type

class CommandException(override val message: String?) : Exception() {
    constructor() : this(null)
}