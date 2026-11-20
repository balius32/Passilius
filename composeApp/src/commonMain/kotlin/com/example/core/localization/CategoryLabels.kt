package com.example.core.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import passilius.composeapp.generated.resources.*

val LocalCategoryIdentities = compositionLocalOf<Map<String, String>> { emptyMap() }

/** Only explicitly marked built-ins are translated; legacy and renamed categories are user data. */
@Composable
fun categoryLabel(name: String): String {
    if (LocalCategoryIdentities.current[name] != "builtin:$name") return name
    val resource = when (name) {
        "Personal" -> Res.string.category_personal
        "Work" -> Res.string.category_work
        "Finance" -> Res.string.category_finance
        "Entertainment" -> Res.string.category_entertainment
        "Social" -> Res.string.category_social
        else -> return name
    }
    return tr(resource)
}
