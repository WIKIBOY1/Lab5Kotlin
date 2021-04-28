import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement

/**
 * Класс для координат квартиры - объектов коллекции
 */
@XmlRootElement(name = "coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
class Coordinates {
    private var x = 0.0
    private var y = 0F //Значение поля должно быть больше -850 = 0f

    constructor(x: Double, y: Float) {
        this.x = x
        this.y = y
    }
    constructor()

    fun getY(): Float{
        return y
    }

    fun getX(): Double{
        return x
    }

    override fun toString(): String {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}'
    }
}