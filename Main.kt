fun main() {

    var input = inputCollector.readFile("C:\\Users\\rdm84\\Documents\\mainInput.txt")
    var output = inputCollector.convertInput(input, mainConfig)
    println(output)

   // var config = inputCollector.newCustomConfig(mutableListOf(" ", ";", "\r\n"), ';', mutableListOf("/start/", "/end/"))
   // output = inputCollector.convertInput(input, config)

    var outputTwo = mutableListOf<MutableList<String>>()

    outputTwo = divToStatement(output, "/")
    println(outputTwo)

}

fun divToStatement(input: MutableList<String>, separator: String): MutableList<MutableList<String>> {
    var output = mutableListOf<MutableList<String>>()
    var ioMain = input
    var computable = mutableListOf<MutableList<String>>()
    var pointer = 0
    var marker = 0

    //refines input into something usable in the loop
    ioMain.add("$separator")

    while (pointer < ioMain.size) {
        if (ioMain[pointer] == separator) {
            computable.add( ioMain.subList(marker, pointer) )
            marker = pointer + 1
        }
        pointer += 1
    }

    output = computable
    return output
}

abstract class genericOperator() {
    abstract val name: String
    abstract val type: String

    //has function "execute"
}

abstract class reference(): genericOperator() {
    override val name = "ref"
    override val type = "declaration"

    fun execute(statement: MutableList<String>): value {
        val output = value(statement[statement.indexOf("ref")+1], "Unit", Unit)
        return output
    }
}

abstract class evaluate(): genericOperator() {
    override val name = "="
    override val type = "non_actionable"

    fun execute(statement: MutableList<String>): genericReturnable {

        if (statement.contains("val")) {
            if (!statement.contains("openTag")) { val type = infer(statement) }
            else { val type = statement[statement.indexOf("openTag") + 1] }
        }
        else {

        }



        return Void
    }

    //type inference
    private fun infer(statement: MutableList<String>): String {
        var output = "INVALID"
        if (statement.contains("stringMarker")) { output = "str" }
        else if (statement.contains("charMarker")) { output = "char" }
        else if (statement.contains("decMarker")) { output = "decimal" }
        else if (statement.contains("true") || statement.contains("false")) { output = "bool" }
        else { output = "int" }
        return output
    }

    //collection type inference
    private fun colInfer(statement: MutableList<String>): String {
        var output = "INVALID/COLLECTION"
        if (statement.contains("openSquareBracket")) { output = "arr" }
        else if (statement.contains("openParentheses")) { }
        return output
    }
}

abstract class genericReturnable()

data class value(val name: String, var type: String, var value: Unit): genericReturnable()

//basically just Unit, but with a better name and can be returned by execute methods on operators
class Void(): genericReturnable() {
    companion object container: genericReturnable() {
        val void = Unit
    }
}
