package learn.script

import learn.logger
import java.io.File
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

class ExtDslScriptHost {

    private val log by logger()

    private fun evalFile(scriptFile: File): ResultWithDiagnostics<EvaluationResult> {
        val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<ExtDslScript> {
            jvm {
                // Script will have access to everything in the classpath
                dependenciesFromCurrentContext(wholeClasspath = true)
            }
        }
        return BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), compilationConfiguration, null)
    }

    fun execFile(path: String) {

        log.info("path to File: $path")
        val scriptFile = File(path)
        log.info("Kann das File gelesen werden? ${scriptFile.canRead()}")
        val res = evalFile(scriptFile)

        res.reports.forEach {
            if (it.severity > ScriptDiagnostic.Severity.DEBUG) {
                println(" : ${it.message}" + if (it.exception == null) "" else ": ${it.exception}")
            }
        }
    }
}
