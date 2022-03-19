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

abstract class GenericOperator {
    abstract val name: String
    abstract val type: String

    //has function "execute"
}

abstract class Environment: GenericOperator() {
    override val name = "env"
    override val type = "declaration"

    fun execute(statement: MutableList<String>): Value {
        val output = Value(statement[statement.indexOf("ref")+1], "Unit", Unit)
        return output
    }
}

abstract class Evaluate: GenericOperator() {
    override val name = "="
    override val type = "non_actionable"

    fun execute(statement: MutableList<String>): GenericReturnable {

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
        output = if (statement.contains("stringMarker")) {
            "str"
        } else if (statement.contains("charMarker")) {
            "char"
        } else if (statement.contains("decMarker")) {
            "decimal"
        } else if (statement.contains("true") || statement.contains("false")) {
            "bool"
        } else {
            "int"
        }
        return output
    }

    //collection type inference
    private fun colInfer(statement: MutableList<String>): String {
        var output = "INVALID/COLLECTION"
        if (statement.contains("openSquareBracket")) { output = "arr" }
        else if (statement.contains("openParentheses")) { output = "list" }
        return output
    }
}

abstract class GenericReturnable

data class Value(val name: String, var type: String, var value: Unit): GenericReturnable()

//basically just Unit, but with a better name and can be returned by execute methods on operators
class Void: GenericReturnable() {
    companion object Container: GenericReturnable() {
        val void = Unit
    }
}
