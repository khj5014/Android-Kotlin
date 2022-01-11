class Calculater(var first: Int, var second: Int) {
    private var result: Int = 0

    fun sum() {
        this.result = this.first + this.second
    }
    fun minus() {
        this.result = this.first - this.second
    }
    fun multi() {
        this.result = this.first * this.second
    }
    fun divi() {
        this.result = this.first / this.second
    }

    override fun toString(): String {
        return result.toString()
    }
}

fun main(){

    val cal: Calculater = Calculater(9,9)
    println(cal.first)
    println(cal.second)
    cal.sum()
    println(cal)
    cal.multi()
    println(cal)
}