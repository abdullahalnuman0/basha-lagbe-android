package dev.abdullah.messseat.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(

//        primary = Color(0xFF5DD8CC),
        primary = Color(0xFF0A6C64),
        onPrimary = Color(0xFF003733),

        primaryContainer = Color(0xFF005049),
        onPrimaryContainer = Color(0xFF7AF5E8),

        secondary = Color(0xFFB8CCC8),
        onSecondary = Color(0xFF233331),

        tertiary = Color(0xFFB9C6EA),
        onTertiary = Color(0xFF27304A),

        background = Color(0xFF0F172A),
        onBackground = Color(0xFFF8FBF8),

        surface = Color(0xFF1E293B),
        onSurface = Color(0xFFF8FBF8),

        surfaceVariant = Color(0xFF334155),
        onSurfaceVariant = Color(0xFFC0CCC9),

        surfaceContainer = Color(0xFF181829),

        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),

        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),

        outline = Color(0xFF647875)
    )


private val LightColorScheme =
    lightColorScheme(

        primary = Color(0xFF0A6C64),
        onPrimary = Color(0xFFFFFFFF),

        primaryContainer = Color(0xFF8AF3E7),
        onPrimaryContainer = Color(0xFF00201D),

        secondary = Color(0xFF506360),
        onSecondary = Color(0xFFFFFFFF),

        tertiary = Color(0xFF59627D),
        onTertiary = Color(0xFFFFFFFF),

        background = Color(0xFFF8FBF8),
        onBackground = Color(0xFF181A18),

        surface = Color(0xFFF8FBF8),
        onSurface = Color(0xFF181A18),

        surfaceVariant = Color(0xFFDCE5E2),
        onSurfaceVariant = Color(0xFF404947),

        surfaceContainer = Color(0xFFFFFFFF),

        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),

        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),

        outline = Color(0xFF707976)
    )
@Immutable
data class ExtendedColors(
    val success: Color,
    val warning: Color,
    val danger: Color,
    val accent: Color,
    val cardBorder: Color,
    val track: Color,
    val isDark: Boolean,
)

 val DarkExtended = ExtendedColors(
    success = MintBright,
    warning = AmberBright,
    danger = DangerBright,
    accent = Coral,
    cardBorder = Color.White.copy(alpha = 0.06f),
    track = Color.White.copy(alpha = 0.08f),
    isDark = true,
)

 val LightExtended = ExtendedColors(
    success = Mint,
    warning = Amber,
    danger = Danger,
    accent = Color(0xFFE0507A),
    cardBorder = Color(0xFF16162A).copy(alpha = 0.06f),
    track = Color(0xFF16162A).copy(alpha = 0.08f),
    isDark = false,
)

@Composable
fun MessSeatTheme(
    darkTheme: Boolean,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val LocalExtendedColors = staticCompositionLocalOf { DarkExtended }

object AppThemeExt {
    val colors: ExtendedColors
        @Composable get() = LocalExtendedColors.current
}