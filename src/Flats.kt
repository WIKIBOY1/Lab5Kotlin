import java.util.TreeSet
import java.time.LocalDate
import java.util.Comparator
import java.util.HashMap
import javax.xml.bind.annotation.*

/**
 * Класс, с помощью которого осуществляется перевод из xml в java object и обратно, и в котором хранятся данные.
 */
@XmlRootElement(name = "flats")
@XmlAccessorType(XmlAccessType.NONE)
class Flats() {
    @XmlElement(name = "flat")
    private var flats = TreeSet(Comparator.comparing { obj: Flat -> obj.getId() })

    @XmlTransient
    var creationDate: LocalDate

    @XmlTransient
    var creationDateString: String

    var iterator: Iterator<Flat> = flats.iterator()

    companion object {
        var dictionary: MutableMap<String, String> = HashMap()

        fun getDictionary(): HashMap<String, String> {
            return dictionary as HashMap<String, String>
        }

        init {
            dictionary["info "] =
                " выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)."
            dictionary["show "] =
                " выводит в стандартный поток вывода все элементы коллекции в строковом представлении."
            dictionary["add {element} "] = " добавляет новый элемент в коллекцию."
            dictionary["update id {element} "] = " обновляет значение элемента коллекции, id которого равен заданному."
            dictionary["remove_by_id id "] = " удаляет элемент из коллекции по его id."
            dictionary["clear "] = " очищает коллекцию."
            dictionary["save "] = " сохраняет коллекцию в файл."
            dictionary["execute_script file_name "] =
                " считывает и исполняет скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме."
            dictionary["exit "] = " завершает программу (без сохранения в файл)."
            dictionary["remove_greater {element} "] = " удаляет из коллекции все элементы, превышающие заданный."
            dictionary["remove_lower {element} "] = " удаляет из коллекции все элементы, меньшие, чем заданный."
            dictionary["history "] = " выводит последние 9 команд (без их аргументов)."
            dictionary["remove_all_by_house house "] =
                " удаляет из коллекции все элементы, значение поля house которого эквивалентно заданному."
            dictionary["filter_contains_name name "] =
                " выводит элементы, значение поля name которых содержит заданную подстроку."
            dictionary["print_ascending "] = " выводит элементы коллекции в порядке возрастания."
        }
    }

    fun getFlats():TreeSet<Flat>{
        return flats
    }

    fun setFlats(newFlats :TreeSet<Flat>){
        flats = newFlats
    }

    override fun toString(): String {
        var flatsString = ""
        for (flat in flats) {
            flatsString += """
                $flat
                """.trimIndent()+"\n"
        }
        return flatsString
    }

    init {
        creationDate = LocalDate.now()
        creationDateString = creationDate.toString()
    }
}