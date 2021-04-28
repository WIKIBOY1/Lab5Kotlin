import java.util.*

/**
 * Класс, хранящий последние 9 введённых команд
 */
class CommandsHistory {
    private val commandsHistory: MutableList<String> = ArrayList()
    fun getCommandsHistory() {
        println(commandsHistory)
    }

    fun addPlus(command: String) {
        if (commandsHistory.size == 9) {
            commandsHistory.removeAt(0)
        }
        commandsHistory.add(command)
    }

    override fun toString(): String {
        return commandsHistory.toString()
    }
}