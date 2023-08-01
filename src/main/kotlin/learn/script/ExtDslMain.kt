package learn.script

fun main(vararg args: String) {
    if (args.size != 1) {
        println("usage: <app> <script file>")
    } else {
        ExtDslScriptHost().execFile(args[0])
    }
}
