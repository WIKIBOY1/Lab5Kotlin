import java.time.LocalDateTime
import javax.xml.bind.annotation.*

/**
 * Объекты класса Flat, которые хранятся в коллекции
 */
@XmlRootElement(name = "flat")
@XmlAccessorType(XmlAccessType.FIELD)
class Flat {
    @XmlTransient
    private var id //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
            : Int = 1
    private var name //Поле не может быть null, Строка не может быть пустой
            : String = ""
    lateinit var coordinates //Поле не может быть null
            : Coordinates

    @XmlElement(name = "creationDate")
    private var dateTimeString: String? = null

    @XmlTransient
    lateinit var creationDate //Поле не может быть null, Значение этого поля должно генерироваться автоматически
            : LocalDateTime
        private set
    private var area //Значение поля должно быть больше 0
            : Float = 1F
    var numberOfRooms //Значение поля должно быть больше 0
            : Long = 1
        private set
    lateinit var furnish //Поле не может быть null
            : Furnish
    lateinit var view //Поле не может быть null
            : View
    var transport //Поле может быть null
            : Transport? = null
    var house //Поле может быть null
            : House? = null

    constructor() {
        id = statId
        setCreationDate(LocalDateTime.now())
        setDateTimeString(creationDate.toString())
        statId++
    }

    constructor(
        name: String,
        coordinates: Coordinates,
        area: Float,
        numberOfRooms: Long,
        furnish: Furnish,
        view: View,
        transport: Transport?,
        house: House?
    ) {
        setName(name)
        id = statId
        this.coordinates = coordinates
        setCreationDate(LocalDateTime.now())
        setArea(area)
        setNumberOfRooms(numberOfRooms)
        this.furnish = furnish
        this.view = view
        this.transport = transport
        this.house = house
        setDateTimeString(creationDate.toString())
        statId++
    }

    fun setDateTimeString(dateTimeString: String?) {
        this.dateTimeString = dateTimeString
    }

    fun getId(): Int {
        return id
    }

    fun getName(): String {
        return name
    }

    override fun toString(): String {
        val houseString: String
        houseString = if (house == null) {
            ", дома нет("
        } else {
            (", имя дома = " + house!!.getName() + ", год постройки = " + house!!.getYear()
                    + ", количество квартир на этаже = " + house!!.getNumberOfFlatsOnFloor())
        }
        return ("id = " + getId() + ", имя = " + getName() + ", координата x = " + coordinates.getX()
                + ", координата y = " + coordinates.getY() + ", время создания = " + creationDate
                + ", площадь = " + area + ", число комнат = " + numberOfRooms + ", состояние мебели = "
                + furnish + ", вид = " + view + ", количество транспорта = " + transport
                + houseString)
    }

    fun setName(name: String?) {
        if (name != null) {
            this.name = name
        } else {
            println("Имя дома должно иметь знаение")
        }
    }

    fun setId(id: Int) {
        this.id = id
        statId--
    }

    fun setCreationDate(creationDate: LocalDateTime) {
        this.creationDate = creationDate
        dateTimeString = creationDate.toString()
    }

    fun setArea(area: Float) {
        if (area > 0) {
            this.area = area
        } else {
            println("Имя дома должно иметь знаение")
        }
    }

    fun setNumberOfRooms(numberOfRooms: Long) {
        if (numberOfRooms > 0) {
            this.numberOfRooms = numberOfRooms
        } else {
            println("Имя дома должно иметь знаение")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val flat = other as Flat
        return id == flat.id &&
                name == flat.name &&
                coordinates == flat.coordinates &&
                dateTimeString == flat.dateTimeString &&
                creationDate == flat.creationDate &&
                area == flat.area &&
                numberOfRooms == flat.numberOfRooms && furnish == flat.furnish && view == flat.view && transport == flat.transport &&
                house == flat.house
    }

    override fun hashCode(): Int {
        return if (house == null){ (area + numberOfRooms).toInt()
        } else ((area + numberOfRooms + house!!.getYear() + house!!.getNumberOfFlatsOnFloor()).toInt())
    }

    companion object {
        var statId = 1
            private set
    }
}