package com.example.core.localization

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.getPluralString
import passilius.composeapp.generated.resources.*

data class UiMessage(val resource: StringResource, val arguments: List<Any> = emptyList()) {
    constructor(resource: StringResource, vararg arguments: Any) : this(resource, arguments.toList())
    fun resolve(): String = tr(resource, *arguments.toTypedArray())
}

/** Resolves at presentation time; observing language also invalidates Compose callers. */
fun tr(resource: StringResource, vararg arguments: Any): String {
    LanguageController.language
    return runBlocking { getString(resource, *arguments.map { if (it is Number) displayNumber(it) else it }.toTypedArray()) }
}

fun displayNumber(value: Number): String = if (LanguageController.language == "fa") {
    value.toString().map { if (it in '0'..'9') ('۰'.code + (it - '0')).toChar() else it }.joinToString("")
} else value.toString()

fun plural(resource: PluralStringResource, count: Int): String {
    LanguageController.language
    return runBlocking { getPluralString(resource, count, displayNumber(count)) }
}

fun gradeText(grade: String): String = tr(when (grade) {
    "Weak" -> Res.string.grade_weak
    "Fair" -> Res.string.grade_fair
    "Strong" -> Res.string.grade_strong
    "Military-Grade" -> Res.string.grade_military
    else -> Res.string.grade_empty
})
