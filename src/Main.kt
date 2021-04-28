import kotlin.jvm.JvmStatic

/**
 * @author Ivanov Georgii R3140
 */

object Main{
    @JvmStatic
    fun main(args: Array<String>) {
        val viwer = Viwer(Presenter(), System.`in`, false)
        viwer.analyzePath()
        viwer.fillUp()
        viwer.init()
    }
}