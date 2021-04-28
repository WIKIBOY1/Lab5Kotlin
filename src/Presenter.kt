import Flats.Companion.getDictionary
import java.io.*
import java.util.*

/**
 * Класс реализующий команды и первод xml to java object и наоборот
 */
@Suppress("JAVA_CLASS_ON_COMPANION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Presenter {
    private var flats: Flats? = null
    var isScriptFlag = false
    private var zeroCollection: File? = null

    fun setZeroCollection(zeroCollection: File?) {
        this.zeroCollection = zeroCollection
    }

    fun getZeroCollection():File? {
        return zeroCollection
    }

    fun getFlats(): TreeSet<Flat> {
        return flats!!.getFlats()
    }

    fun setFlats(flats: Flats?) {
        this.flats = flats
    }

    /**
     * Выводит информацию о коллекции
     */
    fun info(): String {
        return """
            Количество элементов : ${flats!!.getFlats().size}
            Дата инициализации : ${flats!!.creationDateString}
            Тип коллекции : ${Flats::class}
            """.trimIndent()
    }

    /**
     * Выводит список доступных команд для работы с коллекцией
     */
    fun help(): Map<String, String> {
        return getDictionary()
    }

    /**
     * Выводит элементы коллекции flats
     */
    fun show(): String {
        return flats.toString()
    }

    /**
     * Очищает коллекцию flats
     */
    fun clear() {
        flats!!.getFlats().clear()
    }

    /**
     * Выводит все элементы коллекции flats, которые содержат в имени вводимую подстроку name
     * @param name - подстрока, которую должно содержать имя
     */
    @Throws(NoneFlat::class)
    fun filter_contains_name(name: String?): TreeSet<Flat> {
        val newFlats = TreeSet(Comparator.comparing { obj: Flat -> obj.getId() })
        var i = 0
        for (flat in flats!!.getFlats()) {
            if (flat.getName().contains(name!!)) {
                i++
                newFlats.add(flat)
            }
        }
        if (i == 0) {
            throw NoneFlat("Ничего не найдено")
        }
        return newFlats
    }

    /**
     * Добавляет элемент в коллекцию
     */
    fun add(flat: Flat) {
        flats!!.getFlats().add(flat)
    }

    /**
     * Добавляет координату X
     */
    fun addX(inputString: String): Double {
        return if (inputString.isNotEmpty()) {
            inputString.toDouble()
        } else {
            throw IncorrectInputException("Введите что-нибудь")
        }
    }

    /**
     * Добавляет координату Y
     */
    fun addY(inputString: String): Float {
        return if (inputString.isNotEmpty()) {
            val y = inputString.toFloat()
            if (y > -850) {
                y
            } else {
                throw IncorrectInputException("Введёные данные не коректны")
            }
        } else {
            throw IncorrectInputException("Введите что-нибудь")
        }
    }

    /**
     * Добавляет имя
     */
    fun addName(inputString: String): String {
        return if (inputString.isNotEmpty()) {
            inputString
        } else {
            throw IncorrectInputException("Введите что-нибудь")
        }
    }

    /**
     * Добавляет площадь
     */
    fun addArea(inputString: String): Float {
        return if (inputString.isNotEmpty()) {
            val area1 = inputString.toFloat()
            if (area1 > 0) {
                area1
            } else {
                throw IncorrectInputException("Введёные данные не коректны")
            }
        } else {
            throw IncorrectInputException("Введите что-нибудь")
        }
    }

    /**
     * Добавляет переменную типа Long(numberOfRooms, year, numberOfFlatsOnFloor)
     */
    fun addLong(inputString: String): Long {
        return if (inputString.isNotEmpty()) {
            val longNumber = inputString.toLong()
            if (longNumber > 0) {
                longNumber
            } else {
                throw IncorrectInputException("Вы ввели неверные данные, значение должно быть больше 0")
            }
        } else {
            throw IncorrectInputException("Введите что-нибудь")
        }
    }

    /**
     * Добавляет вид
     */
    fun addView(inputString: String): View {
        return if (inputString.isNotEmpty()) {
            val intNumber = inputString.toInt()
            if (intNumber in 1..3) {
                View.values()[intNumber - 1]
            } else {
                throw IncorrectInputException("Вы ввели неверные данные, аргумент должен принимать значения от 1 до 3")
            }
        } else {
            throw IncorrectInputException("Введите что-нибудь")
        }
    }

    /**
     * Добавляет количество транспарта
     */
    fun addTransport(inputString: String): Transport? {
        return if (inputString.isNotEmpty()) {
            val intNumber = inputString.toInt()
            if (intNumber in 0..5) {
                if (intNumber > 0) {
                    Transport.values()[intNumber - 1]
                } else {
                    null
                }
            } else {
                throw IncorrectInputException("Вы ввели неверные данные, аргумент должен принимать значения от 0 до 5")
            }
        } else {
            throw IncorrectInputException("Введите что-нибудь")
        }
    }

    /**
     * Добавляет состояние мебели
     */
    fun addFurnish(inputString: String): Furnish {
        return if (inputString.isNotEmpty()) {
            val intNumber = inputString.toInt()
            if (intNumber in 1..5) {
                Furnish.values()[intNumber - 1]
            } else {
                throw IncorrectInputException("Вы ввели неверные данные, аргумент должен принимать значения от 1 до 5")
            }
        } else {
            throw IncorrectInputException("Введите что-нибудь")
        }
    }

    /**
     * Добавляет что-то(квартиру или попытку ввести данные заново)
     */
    fun addSomething(inputString: String): Boolean {
        return if (inputString.isNotEmpty()) {
            when (inputString.toInt()) {
                1 -> {
                    true
                }
                2 -> {
                    false
                }
                else -> {
                    throw IncorrectInputException("Неверный ответ. Попробуйте ещё раз")
                }
            }
        } else {
            throw IncorrectInputException("Введите что-нибудь")
        }
    }

    /**
     * Обновляет элемент с вводимым id
     * @param id - id элемента
     */
    fun updateId(id: Int, flat: Flat) {
        var i = 0
        val flats1 = TreeSet(flats!!.getFlats())
        for (flat1 in flats1) {
            if (flat1.getId() == id) {
                flats!!.getFlats().remove(flat1)
                flat.setId(id)
                flats!!.getFlats().add(flat)
                i++
            }
        }
        if (i == 0) {
            throw IdNotFoundException("Записи с таким id не существует")
        }
    }

    /**
     * Удаляет элемент из коллекции с вводимым id
     * @param identify - id элемента
     */
    fun remove_by_id(identify: Int) {
        var i = 0
        val flats1 = TreeSet(flats!!.getFlats())
        for (flat in flats1) {
            if (flat.getId() == identify) {
                flats!!.getFlats().remove(flat)
                i++
            }
        }
        if (i == 0) {
            throw IdNotFoundException("Записи с таким id не существует")
        }
    }

    /**
     * Выводит все элементы коллекции flats, у которых совпал объект House с вводимым house
     * @param house - дом, в котором находится квартира
     */
    fun remove_all_by_house(house: House?) {
        var i = 0
        val newFlats = TreeSet(flats!!.getFlats())
        for (flat in newFlats) {
            if (flat.house == house) {
                flats!!.getFlats().remove(flat)
                i++
            }
        }
        if (i == 0) {
            throw IdNotFoundException("Дома с такими данными нет")
        }
    }

    /**
     * Удаляет все элементы коллекции, у которых hashcode больше заданного
     */
    fun remove_greater(flat: Flat) {
        remove(true, flat)
    }

    /**
     * Внутренний метод, который занимается удалением элемента коллекции с большим или меньшим hashcode, чем у заданного
     * @param greatest - переменная отвечаящая за удаление больших или меньших элементов
     */
    private fun remove(greatest: Boolean, flat: Flat) {
        var i = 0
        val newFlats = ArrayList(flats!!.getFlats())
        if (greatest) {
            for (flat1 in newFlats) {
                if (flat1.hashCode() > flat.hashCode()) {
                    flats!!.getFlats().remove(flat1)
                    i++
                }
            }
            if (i == 0) {
                throw NoneFlat("Больше квартир нет")
            }
        } else {
            for (flat1 in newFlats) {
                if (flat1.hashCode() < flat.hashCode()) {
                    flats!!.getFlats().remove(flat1)
                    i++
                }
            }
            if (i == 0) {
                throw NoneFlat("Меньше квартир нет")
            }
        }
    }

    /**
     * Выполняет скрипт программ, содержащихся в файле
     * @param filename - имя файла
     */
    @Throws(FileNotFoundException::class)
    fun execute_script(filename: String) {
        if (!isScriptFlag) {
            val inputStream: InputStream = FileInputStream(filename)
            isScriptFlag = true
            val my = Viwer(Presenter(), inputStream, isScriptFlag)
            my.presenter.setFlats(flats)
            my.init()
        }
        isScriptFlag = false
    }

    /**
     * Удаляет все элементы коллекции, у которых hashcode меньше заданного
     */
    fun remove_lower(flat: Flat) {
        remove(false, flat)
    }

    /**
     * Выводит элементы коллекции в порядке возрастания
     */
    fun print_ascending(): ArrayList<Flat> {
        val flatsExample = ArrayList(flats!!.getFlats())
        val newFlats = ArrayList<Flat>()
        while (flatsExample.size > 0) {
            var flat = flatsExample[0]
            for (flat1 in flatsExample) {
                if (flat1.hashCode() <= flat.hashCode()) {
                    flat = flat1
                }
            }
            newFlats.add(flat)
            flatsExample.remove(flat)
        }
        return newFlats
    }

    override fun toString(): String {
        return Flats.javaClass.toString() + ""
    }
}