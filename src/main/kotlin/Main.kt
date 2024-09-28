fun main() {
    val enigma = enigma {
        alphabet("abcdefghijklmnopqrstuvwxyz .")

        rotorsChain {
            firstRotor(
                'a' to 'b',
                'c' to 'd',
                'e' to 'f',
                'g' to 'h',
                'i' to 'j',
                'k' to 'l',
                'm' to 'n',
                'o' to 'p',
                'q' to 'r',
                's' to 't',
                'u' to 'v',
                'w' to 'x',
                'y' to 'z',
                ' ' to '.',
            )
            rotor(
                'a' to 'i',
                'b' to 'o',
                'c' to 'p',
                'd' to 'd',
                'e' to 'e',
                'f' to 'z',
                'g' to 'k',
                'h' to 'l',
                'i' to 's',
                'j' to 't',
                'k' to 'a',
                'l' to 'w',
                'm' to '.',
                'n' to 'y',
                'o' to 'x',
                '.' to 'g',
                'q' to 'h',
                'r' to 'u',
                's' to 'j',
                't' to 'v',
                'u' to 'b',
                'v' to 'c',
                'w' to 'q',
                'x' to 'r',
                'y' to 'm',
                'z' to 'n',
                ' ' to 'f',
                'p' to ' ',
            )
        }
    }


    while (true) {
        val result = readln()
            .asSequence()
            .map { enigma(it) }
            .joinToString("")
        println(result)
    }
}
