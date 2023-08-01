package learn.script

import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

@KotlinScript(
    fileExtension = "extdsl.kts",
    compilationConfiguration = ExtDslScript.ExtDslScriptConfiguration::class
)
abstract class ExtDslScript {

    object ExtDslScriptConfiguration: ScriptCompilationConfiguration({
        defaultImports(
            "learn.dsl.calculation.calculate"
        )
    })
}
