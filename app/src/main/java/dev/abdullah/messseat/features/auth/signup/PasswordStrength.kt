package dev.abdullah.messseat.features.auth.signup

enum class PasswordStrength(val label: String, val score: Float) {
    EMPTY("", 0f),
    WEAK("Weak", 0.25f),
    FAIR("Fair", 0.5f),
    MEDIUM("Medium", 0.75f),
    STRONG("Strong", 1f)
}