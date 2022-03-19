import java.io.File

interface io {
    // what an incredible interface
    fun readFile(fileName: String): String
    fun convertInput(input: String, config: genericConfig): Any
    fun newCustomConfig(separators: MutableList<String>, lineSeparators: Char, codeBuffers: MutableList<String>): customConfig.container
}


//configs for tokenizing and converting input
open class genericConfig {
    open val separators: MutableList<String> = mutableListOf()
    open val lineSeparator: Char = '\u0000'
    open val codeBuffers: MutableList<String> = mutableListOf()
    open val syntaxToReplace = mutableListOf<Char>()
    open val strReplacement = mutableListOf<String>()
}

class mainConfig: genericConfig() {
    companion object container: genericConfig() {
        override val separators = mutableListOf(" ", "\r\n")
        override val lineSeparator = '/'
        override val codeBuffers = mutableListOf("[start]", "[end]")
        override val syntaxToReplace = mutableListOf<Char>('<', '>', '/', '{', '}', '"')
        override val strReplacement = mutableListOf<String>("openTag ", " closeTag", " / ", "openBrace ", " closeBrace", " stringMarker ")
    }
}

class customConfig: genericConfig() {
    companion object container: genericConfig() {
        override var separators = mutableListOf<String>()
        override var lineSeparator = ' '
        override var codeBuffers = mutableListOf<String>()
        override var syntaxToReplace = mutableListOf<Char>()
        override var strReplacement = mutableListOf<String>()
    }
}


object inputCollector: io {
    override fun readFile(fileName: String) = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

    override fun convertInput(input: String, config: genericConfig): MutableList<String> {
        var output: MutableList<String> = mutableListOf()
        var computable = input

        computable = convertToParsable(input, config)
        output = tokenize(computable, config)

        return output
    }

    override fun newCustomConfig(separators: MutableList<String>, lineSeparator: Char, codeBuffers: MutableList<String>): customConfig.container {
        var output = customConfig

        output.separators = separators
        output.lineSeparator = lineSeparator
        output.codeBuffers = codeBuffers

        return output
    }

    //public functions
    //----------------------------------------------------------------------------------
    //private functions

    private fun convertToParsable(input: String, config: genericConfig): String {
        var output = input
        var pointer = 0

        if (!input.contains("[start]")) { output = "[start]\r\n$input" }
        if (!input.contains("[end]")) { output = "$input\r\n[end]" }

        while (pointer < config.syntaxToReplace.size) {
            output = output.replace(config.syntaxToReplace[pointer].toString(), config.strReplacement[pointer])

            pointer += 1
        }

        return output
    }

    private fun tokenize(input: String, config: genericConfig): MutableList<String> {
        var output: Any
        var computable: Any

        //just a shell function to improve readability of code

        computable = normalize(input, config.separators, config.lineSeparator)
        computable = separate(computable, config.lineSeparator)
        computable = convertToTokenList(computable, config.lineSeparator)
        computable = polish(computable, config.codeBuffers, config.lineSeparator)

        output = computable.toMutableList()
        return output
    }

    private fun normalize(input: String, separators: MutableList<String>, replacement: Char): String {
        var output = ""
        var pointer = 0   //generic iteration controller (why use iterator methods when you can do it yourself?)
        var computable = ""

        //this code cleans up input by replacing separators and normalizing spaces

        computable = input.replace("( )+".toRegex(), " ") //this code normalizes spaces

        while (pointer < separators.size) { //this loop takes care of separator replacement
            computable = input.replace("(${separators[pointer]})+".toRegex(), "${replacement} ") //woah
            pointer += 1
        }
        output = computable
        return output
    }

    private fun separate(input: String, separator: Char): MutableList<MutableList<Char>> {
        var output = mutableListOf<MutableList<Char>>()
        var computable = input.toMutableList()
        var marker = mutableListOf<Int>()
        var computableTwo = mutableListOf<MutableList<Char>>()
        var pointer = 0

        //builds a list of indices to separate input by
        while (pointer < computable.size) {
            if (computable[pointer] == separator) { marker.add(pointer) }
            else if (computable[pointer] == ' ') { marker.add(pointer) }
            pointer += 1
        }
        pointer = 1

        //separates indices
        while (pointer < marker.size) {
            computableTwo.add(computable.subList(marker[pointer - 1], marker[pointer]))   //what...
            pointer += 1
        }

        output = computableTwo
        return output
    }

    private fun convertToTokenList(input: MutableList<MutableList<Char>>, tokenSeparator: Char): MutableList<String> {
        var output = mutableListOf<String>()
        var pointer = 0
        var computable = mutableListOf<String>()
        pointer = 0

        var cache: String

        while (pointer < input.size) {
            computable.add("")
            cache = input[pointer].joinToString("")
            cache = cache.replace(" ", "")
            computable[pointer] = cache

            pointer += 1
        }
        output = computable
        return output
    }

    private fun polish(input: MutableList<String>, removeCodeBuffer: MutableList<String>, tokenSeparator: Char): MutableList<String> {
        var computable = input
        var output: MutableList<String>

        computable.removeAll(mutableListOf(""))
        computable.removeAll(removeCodeBuffer)

        if (computable[0] == tokenSeparator.toString()) {
            computable.removeAt(0)
        }
        if (computable.last() == tokenSeparator.toString()) {
            computable.removeLast()
        }

        output = computable
        return output
    }
}
