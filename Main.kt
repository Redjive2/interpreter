import inputCollector

fun main() {

    var input = inputCollector.readFile("(insert .txt file here)")
    var output = inputCollector.tokenize(input, mainConfig)
    println(output)

    var config = inputCollector.newCustomConfig(mutableListOf(" ", ";", "\r\n"), ';', mutableListOf("/start/", "/end/"))
    output = inputCollector.tokenize(input, config)

}