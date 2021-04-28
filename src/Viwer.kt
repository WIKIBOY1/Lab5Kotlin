import java.io.*
import java.util.*
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.bind.UnmarshalException
import kotlin.system.exitProcess

/**
 * Класс, реализующий взаимодействие с пользователем
 */
@Suppress("JAVA_CLASS_ON_COMPANION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Viwer(var presenter: Presenter, inputStream: InputStream, private val scriptFlag: Boolean) {
    private var lastCommand = ""
    private val inputStream: InputStream
    private var lastString: String? = null
    private val commandsHistory = CommandsHistory()
    var path:String =""

    /**
     * Находит путь по введённому значению переменной окружения
     */
    private fun findPath(): String {
        var path = ""
        var m = false
        val input = Scanner(System.`in`)
        var inputString = ""
        var support: Scanner
        do {
            println("Введите переменную окружения файла")
            try {
                if (!input.hasNextLine()) {
                    input.close()
                    try {
                        safeExit(path)
                    } catch (e: NullPointerException) {
                        System.err.println("Нельзя сохранять пустую коллекцию")
                    }
                }
                inputString = input.nextLine()
            } catch (e: IllegalStateException) {
                System.err.println("Не сегодня")
                exitProcess(0)
            }
            if (inputString.isNotEmpty()) {
                support = Scanner(inputString)
                if (support.hasNext()) {
                    path = support.next()
                    m = true
                }
            }
        } while (!m)
        return path
    }

    /**
     * Анализирует на коректность введённого значения переменной окружения для ввода файла
     */
    fun analyzePath() {
        val path = findPath()
        try {
            val file = File(System.getenv(path))
            presenter.setZeroCollection(file)
            if (!presenter.getZeroCollection()!!.exists()) {
                println("Файла по указанному пути не существует.")
                println(System.getenv(path))
                exitProcess(1)
            }
            this.path = path
        } catch (e: NullPointerException) {
            System.err.println("Неверная переменная окружения")
            tryAgain()
            this.path = ""
        } catch (e: JAXBException) {
            e.printStackTrace()
        }
    }

    /**
     * Даёт шанс пользователю снова ввести значение переменной окружния
     */
    private fun tryAgain() {
        println("Хотите попытаться ввести ещё раз(1 - Да, 2 - Нет)?")
        val newScanner = Scanner(System.`in`)
        try {
            if (presenter.addSomething(check(newScanner))) {
                analyzePath()
            } else {
                println("Было приятно с вами поработать")
                safeExit(path)
                exitProcess(0)
            }
        }catch (e: NumberFormatException) {
            System.err.println("Вы ввели неверные данные")
            tryAgain()
        }
    }

    /**
     * Сохраняет коллекцию в xml файл
     */
    private fun save(path : String) {
        try {
            val flats1 = Flats()
            flats1.setFlats(presenter.getFlats())
            val jaxbContext = JAXBContext.newInstance(Flats::class.java)
            val jaxbMarshaller = jaxbContext.createMarshaller()
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, java.lang.Boolean.TRUE)
            val fileOutputStream = FileOutputStream(File(System.getenv(path)))
            jaxbMarshaller.marshal(flats1, fileOutputStream)
        } catch (e: JAXBException) {
            System.err.println("Вы даже не открыли файл(")
        } catch (e: FileNotFoundException) {
            System.err.println("Вы даже не открыли файл(")
        } catch (e: NullPointerException) {
            System.err.println("Неверная переменная окружения")
            tryAgain()
        }
    }

    /**
     * Безопасный выход. Сохраняет коллекцию в xml файл и выходит из программы.
     */
    fun safeExit(path: String) {
        if (path == ""){
            exitProcess(0)
        }
        save(path)
        exitProcess(0)
    }

    /**
     * Десериализует коллекцию из файла xml
     */
    @Throws(JAXBException::class)
    fun fillUp() {
        try {
            if (!presenter.getZeroCollection()!!.canRead() || !presenter.getZeroCollection()!!.canWrite()) throw SecurityException()
        } catch (ex: SecurityException) {
            println("Не хватает прав доступа для работы с файлом.")
            exitProcess(1)
        }
        try {
            val bufferedInputStream = BufferedInputStream(FileInputStream(presenter.getZeroCollection()))
            val context = JAXBContext.newInstance(Flats::class.java)
            val unmarshaller = context.createUnmarshaller()
            val newFlats = unmarshaller.unmarshal(bufferedInputStream) as Flats
            presenter.setFlats(newFlats)
            val iterator = presenter.getFlats().iterator()
            println("Программа готова к работе")
        }catch (e: UnmarshalException) {
            System.err.println("Файл должен быть формата xml")
            tryAgain()
        }catch (e: NullPointerException) {
            System.err.println("В файле не было коллекции")
            tryAgain()
        } catch (e: FileNotFoundException) {
            System.err.println("В файле не было коллекции")
            tryAgain()
        }
    }

    /**
     * Проверка на конец потока вывода(Ctrl + D)
     */
    private fun check(`in`: Scanner): String {
        return try {
            `in`.nextLine().trim { it <= ' ' }.also { lastString = it }
        } catch (ex: NoSuchElementException) {
            System.err.println("Хорошая попытка. Попроробуйте что-нибудь посложнее")
            `in`.close()
            safeExit(path)
            ""
        }
    }

    /**
     * Добавление квартиры
     */
    private fun addFlat(`in`: Scanner): Flat {
        var nameFlat = ""
        var X = 0.0
        var Y = 0f
        var i = 0
        var area1 = 0f
        var furnish: Furnish = Furnish.BAD
        var view: View =View.GOOD
        var transport: Transport? = null
        var numberOfRooms: Long = 0
        var house1: House? = null
        var m = false
        do {
            try {
                if (i == 0) {
                    println("Введите название квартиры: ")
                    nameFlat = presenter.addName(check(`in`))
                    i++
                }
                if (i == 1) {
                    println("Введите координату x: ")
                    X = presenter.addX(check(`in`))
                    i++
                }
                if (i == 2) {
                    println("Введите координату y(больше -850): ")
                    Y = presenter.addY(check(`in`))
                    i++
                }
                if (i == 3) {
                    println("Введите площадь(больше 0): ")
                    area1 = presenter.addArea(check(`in`))
                    i++
                }
                if (i == 4) {
                    println("Введите количество комнат(больше 0): ")
                    numberOfRooms = presenter.addLong(check(`in`))
                    i++
                }
                if (i == 5) {
                    println("Выберете вид 1-3 (" + Arrays.toString(View.values()) + "): ")
                    view = presenter.addView(check(`in`))
                    i++
                }
                if (i == 6) {
                    println("Выберете транспорт 1-5(" + Arrays.toString(Transport.values()) + ") или 0, если ничего не знаете о транспорте: ")
                    transport = presenter.addTransport(check(`in`))
                    i++
                }
                if (i == 7) {
                    println("Выберете мебель 1-5(" + Arrays.toString(Furnish.values()) + "): ")
                    furnish = presenter.addFurnish(check(`in`))
                    i++
                }
                if (i == 8) {
                    house1 = addHouse(`in`)
                    m = true
                }
            } catch (e: IncorrectInputException) {
                System.err.println(e.message)
            }
        } while (!m)
        return Flat(nameFlat, Coordinates(X, Y), area1, numberOfRooms, furnish, view, transport, house1)
    }

    /**
     * Добовление дома
     */
    private fun addHouse(`in`: Scanner): House? {
        var year1: Long = 0
        var numberOfFlatsOnFloor1: Long
        var i = 0
        var nameHouse1 = ""
        var house1: House? = null
        var m = false
        do {
            try {
                if(i == 0){
                    println("Хотите ввести дом[Да - 1, Нет - 2]?")
                    if(presenter.addSomething(check(`in`))){
                        i++
                    }else{
                        m = true
                    }
                }
                if (i == 1) {
                    println("Введите имя дома: ")
                    nameHouse1 = presenter.addName(check(`in`))
                    i++
                }
                if (i == 2) {
                    println("Введите год постройки дома(больше 0): ")
                    year1 = presenter.addLong(check(`in`))
                    i++
                }
                if (i == 3) {
                    println("Введите количество квартир на этаже(больше 0): ")
                    numberOfFlatsOnFloor1 = presenter.addLong(check(`in`))
                    i++
                    house1 = House(nameHouse1, year1, numberOfFlatsOnFloor1)
                    m = true
                }
            } catch (e: IncorrectInputException) {
                System.err.println(e.message)
            } catch (e: NumberFormatException) {
                System.err.println("Вы ввели неверные данные")
            }
        } while (!m)
        return house1
    }

    /**
     * Счиывание id из командной строки
     */
    fun readId(`in`: Scanner, stringScanner: Scanner): Int {
        var id = 0
        val control = stringScanner.hasNextInt()
        var m1 = false
        if (control) {
            id = stringScanner.nextInt()
        } else {
            do {
                println("Введите id элемента: ")
                try {
                    val idString = check(`in`)
                    id = idString.toInt()
                    m1 = true
                } catch (e: IncorrectInputException) {
                    System.err.println(e.message)
                } catch (e: NumberFormatException) {
                    System.err.println("Вы ввели неверные данные")
                }
            } while (!m1)
        }
        return id
    }

    /**
     * Реализация командной строки
     */
    fun init() {
        val `in` = Scanner(inputStream)
        do {
            try {
                lastString = `in`.nextLine().trim { it <= ' ' }
            } catch (ex: NoSuchElementException) {
                `in`.close()
                try {
                    safeExit(path)
                } catch (e: NullPointerException) {
                    System.err.println("Нельзя сохранять пустую коллекцию")
                }
            }
            if (lastString!!.isEmpty()) {
                println("Введите команду")
                lastCommand = ""
                continue
            }
            val stringScanner = Scanner(lastString)
            lastCommand = stringScanner.next()
            var control = stringScanner.hasNextInt()
            when (lastCommand) {
                "help" -> {
                    for (entry in presenter.help().entries) {
                        println(entry)
                    }
                    commandsHistory.addPlus("help")
                }
                "add" -> {
                    presenter.add(addFlat(`in`))
                    println("Элемент успешно добавлен")
                    commandsHistory.addPlus("add")
                }
                "show" -> {
                    try {
                        print(presenter.show())
                    } catch (e: NullPointerException) {
                        System.err.println("В введённом файле отсутствовала коллекция")
                    }
                    commandsHistory.addPlus("show")
                }
                "update" -> {
                    try {
                        presenter.updateId(readId(`in`, stringScanner), addFlat(`in`))
                    } catch (e: IdNotFoundException) {
                        System.err.println(e.message)
                    }
                    println("Элемент успешно обновлён")
                    commandsHistory.addPlus("update")
                }
                "remove_by_id" -> {
                    try {
                        presenter.remove_by_id(readId(`in`, stringScanner))
                        println("Элемент успешно удалён")
                    } catch (e: IdNotFoundException) {
                        System.err.println(e.message)
                    }
                    commandsHistory.addPlus("remove_by_id")
                }
                "save" -> {
                    try {
                        save(findPath())
                        println("Коллекция успешно сохранена")
                    } catch (e: NullPointerException) {
                        System.err.println("Нельзя сохранять пустую коллекцию")
                    }
                    commandsHistory.addPlus("save")
                }
                "remove_lower" -> {
                    try {
                        presenter.remove_lower(addFlat(`in`))
                        println("Квартиры меньше введённой успешно удалены")
                    } catch (e: NoneFlat) {
                        System.err.println(e.message)
                    }
                    commandsHistory.addPlus("remove_lower")
                }
                "print_ascending" -> {
                    println(presenter.print_ascending())
                    commandsHistory.addPlus("print_ascending")
                }
                "filter_contains_name" -> {
                    control = stringScanner.hasNext()
                    var m3 = false
                    if (control) {
                        val name = stringScanner.next()
                        try {
                            println(presenter.filter_contains_name(name))
                        } catch (e: NoneFlat) {
                            println(e.message)
                        }
                    } else {
                        do {
                            println("Введите имя элемента: ")
                            try {
                                val name = presenter.addName(check(`in`))
                                println(presenter.filter_contains_name(name))
                                m3 = true
                            } catch (e: NoneFlat) {
                                System.err.println(e.message)
                                m3 = true
                            } catch (e: NumberFormatException) {
                                System.err.println("Вы ввели неверные данные")
                            }
                        } while (!m3)
                    }
                    commandsHistory.addPlus("filter_contains_name")
                }
                "remove_all_by_house" -> {
                    try {
                        presenter.remove_all_by_house(addHouse(`in`))
                        println("Все квартиры из введёного дома удалены")
                    } catch (e: NullPointerException) {
                        System.err.println(e.message)
                    }
                    commandsHistory.addPlus("remove_all_by_house")
                }
                "history" -> {
                    commandsHistory.getCommandsHistory()
                    commandsHistory.addPlus("history")
                }
                "remove_greater" -> {
                    try {
                        presenter.remove_greater(addFlat(`in`))
                        println("Квартиры больше введённой успешно удалены")
                    } catch (e: NoneFlat) {
                        System.err.println(e.message)
                    }
                    commandsHistory.addPlus("remove_greater")
                }
                "clear" -> {
                    presenter.clear()
                    println("Коллекция успешно очищена")
                    commandsHistory.addPlus("clear")
                }
                "info" -> {
                    println(presenter.info())
                    commandsHistory.addPlus("info")
                }
                "execute_script" -> {
                    if (!scriptFlag) {
                        control = stringScanner.hasNext()
                        var m4 = false
                        if (control) {
                            val filename = stringScanner.next()
                            try {
                                presenter.execute_script(filename)
                            } catch (e: FileNotFoundException) {
                                System.err.println("Файла по указанному пути не существует")
                            } catch (e: NumberFormatException) {
                                System.err.println("Вы ввели неверные данные")
                            } catch (e: IncorrectInputException) {
                                System.err.println(e.message)
                            }
                        } else {
                            do {
                                println("Введите имя файла: ")
                                try {
                                    val filename = presenter.addName(check(`in`))
                                    presenter.execute_script(filename)
                                    m4 = true
                                } catch (e: FileNotFoundException) {
                                    System.err.println("Файла по указанному пути не существует")
                                    m4 = true
                                } catch (e: NumberFormatException) {
                                    System.err.println("Вы ввели неверные данные")
                                } catch (e: IncorrectInputException) {
                                    System.err.println(e.message)
                                }
                            } while (!m4)
                        }
                    } else {
                        System.err.println("Нельзя использовать execute_script в файле execute_script")
                    }
                    commandsHistory.addPlus("execute_script")
                }
                "exit" -> {
                    println("Всего хорошего")}
                else -> { println("Неизвестная команда. Вы можете посмотреть список команд с помощью 'help'") }
            }
        } while (lastCommand != "exit")
    }

    init {
        this.inputStream = inputStream
    }
}