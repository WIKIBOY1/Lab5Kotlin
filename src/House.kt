import java.util.*
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * Объекты класса House являются домами, в которых находятся квартиры
 */
@XmlRootElement(name = "house")
@XmlAccessorType(XmlAccessType.FIELD)
class House {
    @XmlElement(name = "nameHouse")
    private var name //Поле может быть null
            : String = ""
    private var year //Значение поля должно быть больше
            : Long = 1
    private var numberOfFlatsOnFloor //Значение поля должно быть больше 0
            : Long = 1

    constructor(name: String, year: Long, numberOfFlatsOnFloor: Long) {
        setName(name)
        setYear(year)
        setNumberOfFlatsOnFloor(numberOfFlatsOnFloor)
    }

    fun getName(): String {
        return name
    }

    fun getYear(): Long {
        return year
    }

    fun getNumberOfFlatsOnFloor(): Long {
        return numberOfFlatsOnFloor
    }
    constructor()

    fun setName(name: String) {
        this.name = name
    }

    fun setNumberOfFlatsOnFloor(numberOfFlatsOnFloor: Long) {
        if (numberOfFlatsOnFloor > 0) {
            this.numberOfFlatsOnFloor = numberOfFlatsOnFloor
        } else {
            println("Количество квартир на этаже должно быть больше 0")
        }
    }

    fun setYear(year: Long) {
        if (year > 0) {
            this.year = year
        } else {
            println("Год постройки дома должен быть больше 0")
        }
    }

    override fun toString(): String {
        return "House{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", numberOfFlatsOnFloor=" + numberOfFlatsOnFloor +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val house = other as House
        return name == house.name && year == house.year && numberOfFlatsOnFloor == house.numberOfFlatsOnFloor
    }

    override fun hashCode(): Int {
        return Objects.hash(name, year, numberOfFlatsOnFloor)
    }
}