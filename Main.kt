import inputCollector

fun main() {
    var input = inputCollector.readFile("( insert .txt file here )")
    var output = inputCollector.convertInput(input, mainConfig)
    println(output)

    var config = inputCollector.newCustomConfig(mutableListOf(" ", ";", "\r\n"), ';', mutableListOf("/start/", "/end/",), mutableListOf(true, true, true))
            output = inputCollector.convertInput(input, config)
}
