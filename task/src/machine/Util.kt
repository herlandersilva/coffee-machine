package machine

object Util {
    private var sendToCheck = true

    fun ask(title: String): String {
        println(title)
        return readln().trim()
    }

    fun readln(): String {
        if (!sendToCheck) {
            print("> ")
        }
        return kotlin.io.readln()
    }

    fun disableToSend() { this.sendToCheck = false }
    fun enableToSend() { this.sendToCheck = true }
}